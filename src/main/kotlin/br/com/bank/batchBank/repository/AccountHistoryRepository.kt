package br.com.bank.batchBank.repository

import br.com.bank.batchBank.entity.AccountHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountHistoryRepository : JpaRepository<AccountHistoryEntity, UUID>