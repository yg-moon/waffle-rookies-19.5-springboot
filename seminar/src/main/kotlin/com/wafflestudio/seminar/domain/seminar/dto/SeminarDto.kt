package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.user.dto.InstructorDto
import com.wafflestudio.seminar.domain.user.dto.ParticipantDto
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class SeminarDto {
    data class Response(
        val id: Long,
        val name: String,
        val capacity: Int,
        val count: Int,
        val time: String,
        val online: Boolean,
        val instructors: List<InstructorDto.SeminarResponse>?,
        val participants: List<ParticipantDto.SeminarResponse>?,
    ){
        constructor(seminar: Seminar): this(
            id = seminar.id,
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = seminar.time,
            online = seminar.online,
            instructors = seminar.seminarInstructors?.map{
                InstructorDto.SeminarResponse(it.user)
            },
            participants = seminar.seminarParticipants?.map{
                ParticipantDto.SeminarResponse(it)
            } ?: listOf(),
        )
    }

    data class UserResponse(
        val id: Long,
        val name: String,
        val joined_at: LocalDateTime,
        val is_active: Boolean,
        val dropped_at: LocalDateTime? = null,
    ){
        constructor(seminar: Seminar, seminarParticipant: SeminarParticipant) : this(
            id = seminar.id,
            name = seminar.name,
            joined_at = seminarParticipant.joinedAt,
            is_active = seminarParticipant.isActive,
            dropped_at = seminarParticipant.droppedAt,
        )
    }

    data class CreateRequest(
        @field: NotBlank
        val name: String,

        @field: NotNull
        @field: Min(1)
        val capacity: Int,

        @field: NotNull
        @field: Min(1)
        val count: Int,

        @field: NotNull
        @field: DateTimeFormat(pattern = "HH:mm")
        val time: String,

        val online: String = "true",
    )

    data class ListResponse(
        val id: Long,
        val name: String,
        val instructors: List<InstructorDto.SeminarResponse>?,
        val participant_count: Int,
    ){
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
            instructors = seminar.seminarInstructors?.map{
                InstructorDto.SeminarResponse(it.user)
            },
            participant_count = seminar.seminarParticipants?.count{
                it.isActive
            } ?: 0,
        )
    }

    data class RegisterRequest(
        val role: String,
    )

}