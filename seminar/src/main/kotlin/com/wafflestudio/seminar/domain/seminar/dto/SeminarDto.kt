package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import java.time.LocalDateTime

class SeminarDto {
    data class Response(
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
}