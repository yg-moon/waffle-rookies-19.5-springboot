package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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

    ) : BaseTimeEntity()