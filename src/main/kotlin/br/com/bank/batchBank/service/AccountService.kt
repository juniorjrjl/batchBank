package br.com.bank.batchBank.service

import br.com.bank.batchBank.dto.AccountCSV
import br.com.bank.batchBank.entity.OwnerType
import br.com.bank.batchBank.repository.AccountRepository
import br.com.bank.batchBank.util.getAccountInfo
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.math.BigDecimal

@Service
class AccountService(
    private val repository: AccountRepository,
    private val job: Job,
    private val jobLauncher: JobLauncher
) {

    fun executeBatchTransaction(file: MultipartFile){
        val filename = file.originalFilename?:throw IllegalArgumentException("Invalid file")

        if (file.isEmpty || !filename.endsWith(".csv")){
            throw IllegalArgumentException("Invalid file")
        }

        val (branchNumber, accountNumber, checkNumber) = getAccountInfo(filename.replace(".csv", ""))
        if (accountInfoIsInvalid(accountNumber, branchNumber, checkNumber)){
            throw IllegalArgumentException("Invalid filename")
        }

        repository.findByAccountNumberAndBranchNumberAndCheckNumberAndOwner_OwnerType(
            accountNumber,
            branchNumber,
            checkNumber,
            OwnerType.PJ
        )?.let {
            val accounts = toAccountDTO(file)
            val operationAmount = accounts.map { a -> a.amount }
                .fold(BigDecimal.ZERO) {acc, a -> acc + a }
            if (operationAmount > it.amount){
                throw IllegalArgumentException("Account has no funds to do this operations")
            }
            val jobParameters = JobParametersBuilder()
                .addString("filePath", saveTemp(file, filename))
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters()
            jobLauncher.run(job, jobParameters)
        } ?: throw IllegalArgumentException("Account not found")
    }

    private fun accountInfoIsInvalid(account: String, branch: String, digit: String) =
        account.length != 4 && branch.length != 5 && digit.length != 1

    private fun toAccountDTO(file: MultipartFile): List<AccountCSV> {
        val rows = csvReader{ delimiter = '|' }.readAll(file.inputStream)
        val accounts = rows.drop(1).map { line ->
            AccountCSV(
                name = line[0],
                cpf = line[1].replace(".", "").replace("", ""),
                branch = line[2],
                accountAndNumber = line[3],
                amount = BigDecimal(line[4]),
                monthReference = line[5]
            )
        }
        return accounts
    }

    private fun saveTemp(file: MultipartFile, filename: String): String{
        val dir = File(System.getProperty("java.io.tmpdir"), "batch-bank/temp")
        if (!dir.exists()){
            dir.mkdirs()
        }
        val transferredFile = File(dir, filename)
        file.transferTo(transferredFile)
        return transferredFile.absoluteFile.toString()
    }

}