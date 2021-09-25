package com.wafflestudio.seminar.domain.seminar

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.survey.model.SeminarParticipant
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "seminar")
class Seminar(
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: Int,
    val online: Boolean,

    // val instructors: List<InstructorProfile>,
    // val participants: List<ParticipantProfile>,

    @OneToMany(mappedBy = "seminar")
    val seminarParticipants: List<SeminarParticipant>,

    @OneToMany(mappedBy = "seminar")
    val seminarInstructors: List<InstructorProfile>,


) : BaseTimeEntity()