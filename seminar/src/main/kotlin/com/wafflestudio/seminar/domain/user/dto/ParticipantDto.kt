package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile

class ParticipantDto {
    data class Response(
        val id : Long,
        val university: String,
        val accepted: Boolean,
        val seminars: List<SeminarDto.Response>,
    ){
        constructor(participantProfile: ParticipantProfile) : this(
            id = participantProfile.id,
            university = participantProfile.university,
            accepted = participantProfile.accepted,
            seminars = participantProfile.seminarParticipants?.map{
                SeminarDto.Response(it.seminar, it)
            } ?: listOf()
        )
    }
}