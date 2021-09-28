package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.user.dto.InstructorDto
import com.wafflestudio.seminar.domain.user.dto.ParticipantDto
import java.time.LocalDateTime

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
        val name: String = "",
        val capacity: Int = 0,
        val count: Int = 0,
        val time: String = "",
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
}