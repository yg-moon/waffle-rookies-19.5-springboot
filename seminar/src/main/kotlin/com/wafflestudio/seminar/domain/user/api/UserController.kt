package com.wafflestudio.seminar.domain.user.api

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.service.UserService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.global.auth.CurrentUser
import com.wafflestudio.seminar.global.auth.JwtTokenProvider
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @PostMapping("/")
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest):
            ResponseEntity<UserDto.Response> {
        val user = userService.signup(signupRequest)
        return ResponseEntity.noContent().header("Authentication",
            jwtTokenProvider.generateToken(user.email)).build()
    }

    @GetMapping("/me/")
    fun getCurrentUser(@CurrentUser user: User): UserDto.Response {
        val foundUser = userRepository.findByIdOrNull(user.id)
        // UserDto.Response takes User as param, whereas findByIdOrNull returns User? type.
        var foundUser2: User = user
        if (foundUser != null){
            foundUser2 = foundUser
        }
        return UserDto.Response(foundUser2)
    }

    @GetMapping("/{user_id}/")
    fun getUserById(@CurrentUser user: User,
                    @PathVariable("user_id") userId: Long?): UserDto.Response {
        val foundUser = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException("User not found")
        return UserDto.Response(foundUser)
    }

    @PutMapping("/me/")
    fun editCurrentUser(@CurrentUser user: User,
                        @Valid @RequestBody(required = false) editRequest: UserDto.EditRequest?)
    : UserDto.Response{
        val editedUser = userService.editUser(user, editRequest)
        return UserDto.Response(editedUser)
    }

    @PostMapping("/participant/")
    @ResponseStatus(HttpStatus.CREATED)
    fun editParticipantProfile(@CurrentUser user: User,
                               @Valid @RequestBody participantRequest: UserDto.ParticipantRequest)
    : UserDto.Response{
        val editedUser = userService.editParticipantProfile(user, participantRequest)
        return UserDto.Response(editedUser)
    }


}
