package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "seminar_user")
class User(
    @Column(unique = true)
    @field:NotBlank
    val email: String,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val password: String,

    @Column
    @field:NotNull
    val roles: String = "",

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    val participantProfile: ParticipantProfile?,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    val instructorProfile: InstructorProfile?,

    ) : BaseEntity()
