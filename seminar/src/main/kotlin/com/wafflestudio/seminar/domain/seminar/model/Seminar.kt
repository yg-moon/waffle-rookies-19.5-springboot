package com.wafflestudio.seminar.domain.seminar.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "seminar")
class Seminar(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: String,
    var online: Boolean,

    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.ALL])
    val seminarParticipants: MutableList<SeminarParticipant>? = null,

    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.ALL])
    val seminarInstructors: MutableList<InstructorProfile>? = null,

    ) : BaseTimeEntity()