package com.wafflestudio.seminar.domain.survey.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wafflestudio.seminar.domain.os.dto.OperatingSystemDto
import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.user.model.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class SurveyResponseDto {
    data class Response(
        var id: Long? = 0,
        var os: OperatingSystem? = null,
        var springExp: Int = 0,
        var rdbExp: Int = 0,
        var programmingExp: Int = 0,
        var major: String? = "",
        var grade: String? = "",
        var backendReason: String? = "",
        var waffleReason: String? = "",
        var somethingToSay: String? = "",
        var timestamp: LocalDateTime? = null,
        var user: User? = null // Q9.
    )

    // Q8. POST /api/v1/results/ (설문조사 생성을 위한 DTO)
    data class CreateRequest(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @field:NotNull
        @field:NotBlank
        var os: String = " ",

        @field:NotNull
        @field:Min(1, message = "The value must be between 1 and 5")
        @field:Max(5, message = "The value must be between 1 and 5")
        var spring_exp: Int? = null,

        @field:NotNull
        @field:Min(1, message = "The value must be between 1 and 5")
        @field:Max(5, message = "The value must be between 1 and 5")
        var rdb_exp: Int? = null,

        @field:NotNull
        @field:Min(1, message = "The value must be between 1 and 5")
        @field:Max(5, message = "The value must be between 1 and 5")
        var programming_exp: Int? = null,

        @field:NotBlank
        var major: String? = "",

        @field:NotBlank
        var grade: String? = "",

        var backendReason: String? = "",
        var waffleReason: String? = "",
        var somethingToSay: String? = "",

        @field:NotNull
        var timestamp: LocalDateTime = LocalDateTime.now()

    )

}
