package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.User

class InstructorDto {
    data class UserResponse(
        val id: Long,
        val company: String,
        val year: Int? = null,
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

    data class SeminarResponse(
        val id: Long?,
        val name: String?,
        val email: String?,
        val company: String?,
    ){
        constructor(user: User?) : this(
            id = user?.id,
            name = user?.name,
            email = user?.email,
            company = user?.instructorProfile?.company,
        )
    }
}