package br.com.bank.batchBank.controller

import br.com.bank.batchBank.service.AccountService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("accounts")
class AccountController(
    private val service: AccountService
) {

    @PostMapping
    fun batchTransaction(@RequestParam("file") file: MultipartFile) {
        service.executeBatchTransaction(file)
    }
}