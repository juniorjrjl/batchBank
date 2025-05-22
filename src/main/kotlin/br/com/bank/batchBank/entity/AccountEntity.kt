package br.com.bank.batchBank.entity

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "ACCOUNTS")
class AccountEntity(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID?,
    @Column(nullable = false, columnDefinition = "bpchar(5)")
    var accountNumber: String,
    @Column(nullable = false, columnDefinition = "bpchar(4)")
    var branchNumber: String,
    @Column(nullable = false, columnDefinition = "bpchar(1)")
    var checkNumber: String,
    @Column(nullable = false)
    var amount: BigDecimal,
    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: AccountOwnerEntity,
    @OneToMany(mappedBy = "account", cascade = [ALL], orphanRemoval = true, targetEntity = AccountHistoryEntity::class)
    var histories: MutableSet<AccountHistoryEntity>
) {
    constructor() : this(
        id = null,
        accountNumber = "",
        branchNumber = "",
        checkNumber = "",
        amount = BigDecimal.ZERO,
        owner = AccountOwnerEntity(),
        histories = mutableSetOf<AccountHistoryEntity>()
    )

}
