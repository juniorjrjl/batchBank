package br.com.bank.batchBank.repository

import br.com.bank.batchBank.entity.AccountEntity
import br.com.bank.batchBank.entity.OwnerType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<AccountEntity, UUID>{

    fun findByAccountNumberAndBranchNumberAndCheckNumberAndOwner_OwnerType(
        accountNumber: String,
        branchNumber: String,
        checkNumber: String,
        ownerType: OwnerType): AccountEntity?

}