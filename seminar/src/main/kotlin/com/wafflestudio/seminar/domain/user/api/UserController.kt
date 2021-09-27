package com.wafflestudio.seminar.domain.user.api

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.service.UserService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.global.auth.CurrentUser
import com.wafflestudio.seminar.global.auth.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/")
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest):
            ResponseEntity<UserDto.Response> {
        val user = userService.signup(signupRequest)
        return ResponseEntity.noContent().header("Authentication",
            jwtTokenProvider.generateToken(user.email)).build()
    }

//    @PostMapping("/login/")
//    fun login(): ResponseEntity<UserDto.Response>{
//        return
//    }

    @GetMapping("/me/")
    fun getCurrentUser(@CurrentUser user: User): UserDto.Response {
        return UserDto.Response(user)
    }

    @GetMapping("{user_id}")
    fun getUserById(@CurrentUser user: User,
                    @RequestParam userId: Int): UserDto.Response {
        return UserDto.Response(user)
    }

    @PutMapping("/me/")
    fun editCurrentUser(@CurrentUser user: User,
                        @Valid @RequestBody editRequest: UserDto.EditRequest)
    : UserDto.Response{
        val editedUser = userService.editUser(user, editRequest)
        return UserDto.Response(editedUser)
    }

    @PutMapping("/participant/")
    @ResponseStatus(HttpStatus.CREATED)
    fun editParticipantProfile(@CurrentUser user: User,
                               @Valid @RequestBody participantRequest: UserDto.ParticipantRequest)
    : UserDto.Response{
        val editedUser = userService.editParticipantProfile(user, participantRequest)
        return UserDto.Response(editedUser)
    }


}
