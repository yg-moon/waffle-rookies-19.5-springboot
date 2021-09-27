package com.wafflestudio.seminar.domain.seminar.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "seminar_participant")
class SeminarParticipant(

    val joinedAt: LocalDateTime,

    val isActive: Boolean,

    val droppedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    val participantProfile: ParticipantProfile,

    ) : BaseEntity()