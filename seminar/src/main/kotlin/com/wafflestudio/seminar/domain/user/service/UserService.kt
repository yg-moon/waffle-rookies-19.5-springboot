package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.InvalidRoleException
import com.wafflestudio.seminar.domain.user.exception.InvalidYearException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import java.time.LocalDateTime
import javax.validation.Valid

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun signup(signupRequest: UserDto.SignupRequest): User {
        if (userRepository.existsByEmail(signupRequest.email))
            throw UserAlreadyExistsException()
        val encodedPassword = passwordEncoder.encode(signupRequest.password)

        val role: String = signupRequest.role
        var participantProfile: ParticipantProfile? = null
        var instructorProfile: InstructorProfile? = null
        if (role == "participant"){
            participantProfile = ParticipantProfile(
                signupRequest.university,
                signupRequest.accepted)
        }
        else if (role == "instructor"){
            if (signupRequest.year != null && signupRequest.year <= 0)
                throw InvalidYearException("Year must be higher than 0")
            instructorProfile = InstructorProfile(
                signupRequest.company,
                signupRequest.year)
        }
        else throw InvalidRoleException("Role must be either " +
                "participant or instructor")

        return userRepository.save(User(
            signupRequest.email,
            signupRequest.name,
            encodedPassword,
            role,
            LocalDateTime.now(),
            participantProfile,
            instructorProfile))
    }

    fun editUser(user: User,
                 @Valid @RequestBody editRequest: UserDto.EditRequest): User {
        val savedUser = userRepository.findByEmail(user.email)
            ?: throw UserNotFoundException()

        val role: String = savedUser.roles
        if (role == "participant"){
            savedUser.participantProfile?.university = editRequest.university
        }
        else if (role == "instructor"){
            if (editRequest.year != null && editRequest.year <= 0)
                throw InvalidYearException("Year must be higher than 0")
            savedUser.instructorProfile?.company = editRequest.company
            savedUser.instructorProfile?.year = editRequest.year
        }

        return savedUser
    }



}