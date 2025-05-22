package br.com.bank.batchBank.util

fun getAccountInfo(filename: String): Triple<String, String, String>{
    val parts = filename
        .split("_")
        .takeIf { it.size == 2 }
        ?: throw IllegalArgumentException("Invalid filename")

    val branchNumber = parts[0]
    val accountAndDigit = parts[1].split("-")

    if(accountAndDigit.size != 2){
        throw IllegalArgumentException("Invalid filename")
    }

    return Triple(branchNumber, accountAndDigit[0], accountAndDigit[1])
}