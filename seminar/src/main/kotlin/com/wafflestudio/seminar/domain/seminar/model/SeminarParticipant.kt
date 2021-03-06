package com.wafflestudio.seminar.domain.seminar.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "seminar_participant")
class SeminarParticipant(

    val joinedAt: LocalDateTime,

    var isActive: Boolean,

    var droppedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    val participantProfile: ParticipantProfile?,

    ) : BaseEntity()