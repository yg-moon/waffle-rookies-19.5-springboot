package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @field:NotNull
    @field:NotBlank
    var name: String = "default-name",

    @Column(unique = true)
    @field:NotNull
    @field:NotBlank
    var email: String = "default-email",

) : BaseEntity()