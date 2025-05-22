package br.com.bank.batchBank.dto

import br.com.bank.batchBank.entity.AccountEntity

class AccountsToPersist(val target: AccountEntity, val source: AccountEntity)