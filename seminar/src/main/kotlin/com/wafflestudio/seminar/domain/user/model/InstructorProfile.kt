package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToOne
import javax.persistence.Table
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

    ) : BaseTimeEntity()