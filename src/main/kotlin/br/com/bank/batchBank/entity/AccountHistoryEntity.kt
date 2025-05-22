package br.com.bank.batchBank.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "ACCOUNT_HISTORIES")
class AccountHistoryEntity(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID?,
    @Column(nullable = false)
    var amount: BigDecimal,
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, name = "transaction_type")
    var transactionType: TransactionType,
    @Column(nullable = false, name = "created_at")
    var createdAt: OffsetDateTime,
    @Column(nullable = false)
    var description: String,
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    var account: AccountEntity
) {
    constructor() : this(
        null,
        BigDecimal.ZERO,
        TransactionType.IN,
        OffsetDateTime.now(),
        "",
        AccountEntity())

    constructor(amount: BigDecimal, description: String, account: AccountEntity): this(
        null,
        amount,
        TransactionType.IN,
        OffsetDateTime.now(),
        description,
        account)
}