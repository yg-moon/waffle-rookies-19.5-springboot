package com.wafflestudio.seminar.domain.seminar.repository

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import org.springframework.data.jpa.repository.JpaRepository

interface SeminarRepository: JpaRepository<Seminar, Long?> {

}