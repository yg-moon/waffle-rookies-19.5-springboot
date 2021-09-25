package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
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
    val user: User,

    @OneToMany(mappedBy = "participantProfile")
    val seminarParticipants: List<SeminarParticipant>,

    ) : BaseTimeEntity()