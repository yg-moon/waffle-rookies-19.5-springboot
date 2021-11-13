package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.*
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
                 @Valid @RequestBody editRequest: UserDto.EditRequest?): User {
        val savedUser = userRepository.findByEmail(user.email)
            ?: throw UserNotFoundException("User not found")

        if(editRequest == null) return savedUser

        if (savedUser.participantProfile != null){
            savedUser.participantProfile?.university = editRequest.university
        }
        else if (savedUser.instructorProfile != null){
            if (editRequest.year != null && editRequest.year <= 0)
                throw InvalidYearException("Year must be higher than 0")
            savedUser.instructorProfile?.company = editRequest.company
            savedUser.instructorProfile?.year = editRequest.year
        }

        userRepository.save(savedUser)

        return savedUser
    }

    fun editParticipantProfile(user: User,
                               @Valid @RequestBody participantRequest: UserDto.ParticipantRequest)
    : User{
        val savedUser = userRepository.findByEmail(user.email)
            ?: throw UserNotFoundException()
        if (savedUser.participantProfile != null) throw IsAlreadyParticipant("The user is already a participant")

        savedUser.roles = "participant"
        savedUser.participantProfile = ParticipantProfile(
            participantRequest.university,
            participantRequest.accepted)

        userRepository.save(savedUser)

        return savedUser
    }

}