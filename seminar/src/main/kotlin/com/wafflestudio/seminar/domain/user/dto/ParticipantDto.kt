package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import java.time.LocalDateTime

class ParticipantDto {
    data class UserResponse(
        val id : Long,
        val university: String,
        val accepted: Boolean,
        val seminars: List<SeminarDto.UserResponse>,
    ){
        constructor(participantProfile: ParticipantProfile) : this(
            id = participantProfile.id,
            university = participantProfile.university,
            accepted = participantProfile.accepted,
            seminars = participantProfile.seminarParticipants?.map{
                SeminarDto.UserResponse(it.seminar, it)
            } ?: listOf()
        )
    }

    data class SeminarResponse(
        val id: Long?,
        val name: String?,
        val email: String?,
        val university: String,
        val joined_at: LocalDateTime,
        val is_active: Boolean,
        val dropped_at: LocalDateTime?,
    ){
        constructor(seminarParticipant: SeminarParticipant) : this(
            id = seminarParticipant.participantProfile.user?.id,
            name = seminarParticipant.participantProfile.user?.name,
            email = seminarParticipant.participantProfile.user?.email,
            university = seminarParticipant.participantProfile.university,
            joined_at = seminarParticipant.joinedAt,
            is_active = seminarParticipant.isActive,
            dropped_at = seminarParticipant.droppedAt,
        )
    }
}