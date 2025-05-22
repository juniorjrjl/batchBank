package br.com.bank.batchBank.entity

import jakarta.persistence.CascadeType.ALL
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "ACCOUNT_OWNERS")
class AccountOwnerEntity(
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    var id: UUID?,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false, name = "owner_type")
    @Enumerated(value = EnumType.STRING)
    var ownerType: OwnerType,
    @Column(nullable = false, unique = true)
    var cpf: String,
    @OneToMany(mappedBy = "owner", cascade = [ALL], orphanRemoval = true, targetEntity = AccountEntity::class)
    var accounts: Set<AccountEntity>
) {
    constructor() : this(
        null,
        "",
        OwnerType.PF,
        "",
        HashSet()
    )
}
