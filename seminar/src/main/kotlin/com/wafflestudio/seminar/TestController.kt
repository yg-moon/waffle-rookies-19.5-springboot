package com.wafflestudio.seminar

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class TestController {
    @GetMapping("/ping/")
    @ResponseStatus(HttpStatus.OK)
    fun pong(): String{
        return "pong"
    }
}