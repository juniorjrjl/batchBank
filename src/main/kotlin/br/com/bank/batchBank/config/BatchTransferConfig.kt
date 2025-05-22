package br.com.bank.batchBank.config

import br.com.bank.batchBank.dto.AccountCSV
import br.com.bank.batchBank.dto.AccountsToPersist
import br.com.bank.batchBank.entity.AccountEntity
import br.com.bank.batchBank.entity.AccountHistoryEntity
import br.com.bank.batchBank.entity.OwnerType
import br.com.bank.batchBank.repository.AccountRepository
import br.com.bank.batchBank.util.getAccountInfo
import jakarta.persistence.EntityManagerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager
import java.io.File
import java.math.BigDecimal

@Configuration
class BatchTransferConfig(
    private val platformTransactionManager: PlatformTransactionManager,
    private val accountRepository: AccountRepository,
) {

    @Bean
    fun job(jobRepository: JobRepository, firstStep: Step) = JobBuilder("batch-transfer", jobRepository)
        .start(firstStep)
        .incrementer(RunIdIncrementer())
        .build()

    @Bean
    fun firstStep(
        jobRepository: JobRepository,
        readerCSV: ItemReader<AccountCSV>,
        processor:  ItemProcessor<AccountCSV, AccountsToPersist>,
        dtoWriter: ItemWriter<AccountsToPersist>) =
        StepBuilder("init-step", jobRepository)
            .chunk<AccountCSV, AccountsToPersist>(200, platformTransactionManager)
            .reader(readerCSV)
            .processor(processor)
            .writer(dtoWriter)
            .build()

    @Bean
    @StepScope
    fun readerCSV(@Value("#{jobParameters['filePath']}") filePath: String) =
        FlatFileItemReaderBuilder<AccountCSV>()
            .name("read-csv")
            .resource(FileSystemResource(filePath))
            .linesToSkip(1)
            .delimited()
            .delimiter("|")
            .names("Nome", "CPF", "Agência", "Conta", "Valor", "Mês de Referência")
            .fieldSetMapper { fieldSet ->
                AccountCSV(
                    fieldSet.readString("Nome"),
                    fieldSet.readString("CPF"),
                    fieldSet.readString("Agência"),
                    fieldSet.readString("Conta"),
                    BigDecimal(fieldSet.readString("Valor")),
                    fieldSet.readString("Mês de Referência")
                )
            }
            .build()

    @Bean
    @StepScope
    fun processor(@Value("#{jobParameters['filePath']}") filePath: String): ItemProcessor<AccountCSV, AccountsToPersist> = ItemProcessor { csv ->
        filePath.let {
            val filename = File(it).nameWithoutExtension
            val (branchNumber, accountNumber, checkNumber) = getAccountInfo(filename)
            val originAccount = accountRepository.findByAccountNumberAndBranchNumberAndCheckNumberAndOwner_OwnerType(
                accountNumber,
                branchNumber,
                checkNumber,
                OwnerType.PJ
            )?: throw IllegalArgumentException("Account not found")


            accountRepository.findByAccountNumberAndBranchNumberAndCheckNumberAndOwner_OwnerType(
                csv.account(),
                csv.branch,
                csv.number(),
                OwnerType.PF
            )?.let { account ->
                if (account.owner.name != csv.name && account.owner.cpf != csv.cpf) {
                    throw IllegalArgumentException("Wrong data in account owner")
                }
                account.amount = account.amount.add(csv.amount)
                account.histories.add(
                    AccountHistoryEntity(
                        csv.amount,
                        "payment reference ${csv.monthReference}",
                        account
                    )
                )
                originAccount.amount = originAccount.amount - csv.amount

                AccountsToPersist(account, originAccount)
            } ?: throw IllegalArgumentException("Account not found")
        }
    }

    @Bean
    fun entityWriter(entityManagerFactory: EntityManagerFactory): JpaItemWriter<AccountEntity> =
        JpaItemWriterBuilder<AccountEntity>()
            .entityManagerFactory(entityManagerFactory)
            .usePersist(false)
            .build()


    @Bean
    fun dtoWriter(entityManagerFactory: EntityManagerFactory, entityWriter: JpaItemWriter<AccountEntity>): ItemWriter<AccountsToPersist> =
        ItemWriter { items ->
            items.forEach{
                entityWriter.write(Chunk(it.source))
                entityWriter.write(Chunk(it.target))
            }
        }


    @Bean
    fun fileDeleteStep(jobRepository: JobRepository, deleteFileTasklet: Tasklet) = StepBuilder("delete file", jobRepository)
        .tasklet(deleteFileTasklet, platformTransactionManager)
        .build()

    @Bean
    @StepScope
    fun deleteFileTasklet(@Value("#{jobParameters['filePath']}") filePath: String) =
        Tasklet{
        _, _ ->
            val file = File(filePath)
            if (file.exists()){
                file.delete()
            }
            RepeatStatus.FINISHED
    }

}