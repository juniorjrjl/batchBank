package br.com.bank.batchBank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BatchBankApplication

fun main(args: Array<String>) {
	runApplication<BatchBankApplication>(*args)
}
