package com.wafflestudio.seminar

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
// @EnableJpaAuditing
class SeminarApplication

fun main(args: Array<String>) {
	runApplication<SeminarApplication>(*args)
}
