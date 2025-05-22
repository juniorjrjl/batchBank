package br.com.bank.batchBank.dto

import java.math.BigDecimal


class AccountCSV(
    val name: String,
    val cpf: String,
    val branch: String,
    val accountAndNumber: String,
    val amount: BigDecimal,
    val monthReference: String){

    fun account()= this.accountAndNumber.split("-")[0]

    fun number() = this.accountAndNumber.split("-")[1]
}
