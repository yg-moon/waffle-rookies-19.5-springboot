package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.user.model.User
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val id: Long,
        val name: String,
        val email: String,
        val date_joined: LocalDateTime,
        val participant_profile: ParticipantDto.Response?,
        val instructor_profile: InstructorDto.Response?,
    ) {
        constructor(user: User) : this(
            id = user.id,
            email = user.email,
            name = user.name,
            date_joined = user.dateJoined,
            participant_profile = user.participantProfile?.let { ParticipantDto.Response(it) },
            instructor_profile = user.instructorProfile?.let { InstructorDto.Response(it) },
        )
    }

    data class SignupRequest(
        @field:NotBlank
        val email: String,
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val password: String,
        @field:NotBlank
        val role: String,

        val university: String = "",
        val accepted: Boolean = true,
        val company: String = "",
        val year: Int? = null,
    )

    data class EditRequest (
        val university: String = "",
        val company: String = "",
        val year: Int? = null,
    )

    data class ParticipantRequest(
        val university: String = "",
        val accepted: Boolean = true,
    )
}