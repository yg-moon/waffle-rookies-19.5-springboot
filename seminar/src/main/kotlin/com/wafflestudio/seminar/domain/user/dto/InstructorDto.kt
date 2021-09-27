package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.user.model.InstructorProfile

class InstructorDto {
    data class Response(
        val id: Long,
        val company: String,
        val year: Number? = null,
        val charge: Charge? = null,
    ){
        constructor(instructorProfile: InstructorProfile) : this(
            id = instructorProfile.id,
            company = instructorProfile.company,
            year = instructorProfile.year,
            instructorProfile.seminar?.let { Charge(it) },
        )
    }
    data class Charge(
        val id: Long,
        val name: String,
    ){
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
        )
    }
}