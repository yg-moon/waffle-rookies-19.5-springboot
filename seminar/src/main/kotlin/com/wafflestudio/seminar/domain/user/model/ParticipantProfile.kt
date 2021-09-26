package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "participant_profile")
class ParticipantProfile(
    @field:NotBlank
    val university: String,

    @field:NotBlank
    val accepted: Boolean,

    // @field:NotNull
    // val seminars: List<Seminar>,

    @OneToOne(mappedBy = "participantProfile")
    val user: User? = null,

    @OneToMany(mappedBy = "participantProfile", cascade = [CascadeType.ALL])
    val seminarParticipants: List<SeminarParticipant>? = null,

    ) : BaseTimeEntity()