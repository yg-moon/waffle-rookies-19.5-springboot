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
        var user: User? = null
    )

    data class CreateRequest(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @NotNull
        @NotBlank
        var os: String = " ",

        @NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var spring_exp: Int? = null,

        @NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var rdb_exp: Int? = null,

        @NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var programming_exp: Int? = null,

        var major: String? = "",
        var grade: String? = "",
        var backendReason: String? = "",
        var waffleReason: String? = "",
        var somethingToSay: String? = "",

        @NotNull
        var timestamp: LocalDateTime = LocalDateTime.now(),

        var user_id: Long? = null

    )

}
