package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class UserDto {

    data class Response(
        var name: String = "new-name",
        var email: String = "new-email"
    )

    data class CreateRequest(
        @field:NotNull
        @field:NotBlank
        var name: String = "create-name",

        @field:NotNull
        @field:NotBlank
        var email: String = "create-email",
    )

}