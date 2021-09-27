package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "instructor_profile")
class InstructorProfile(
    var company: String,

    var year: Int?,

    @OneToOne(mappedBy = "instructorProfile", fetch = FetchType.LAZY)
    val user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar? = null,

    ) : BaseTimeEntity()