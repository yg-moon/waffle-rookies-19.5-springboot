package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.Seminar
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "instructor_profile")
class InstructorProfile(
    @field:NotBlank
    val company: String,

    val year: Int?,

    // val charge: Charge?,

    @field:NotNull
    @OneToOne(mappedBy = "instructorProfile", fetch = FetchType.LAZY)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar,

    ) : BaseTimeEntity()