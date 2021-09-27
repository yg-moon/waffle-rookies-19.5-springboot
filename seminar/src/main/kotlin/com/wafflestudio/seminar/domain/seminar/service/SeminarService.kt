package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.InvalidFormException
import com.wafflestudio.seminar.domain.seminar.exception.NotInstructorException
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
) {
    fun createSeminar(user: User, createRequest: SeminarDto.CreateRequest): Seminar{
        if (createRequest.name == "" || createRequest.capacity <= 0 ||
            createRequest.count <= 0 || !isTimeValid(createRequest.time) ||
            !isOnlineValid(createRequest.online)) throw InvalidFormException()

        if (user.roles != "instructor") throw NotInstructorException()

        val toLower = createRequest.online.lowercase()
        var online: Boolean = true
        if (toLower == "false") online = false

        val newSeminar = seminarRepository.save(Seminar(
            createRequest.name,
            createRequest.capacity,
            createRequest.count,
            createRequest.time,
            online,
        ))

        user.instructorProfile?.seminar = newSeminar
        userRepository.save(user)

        return newSeminar
    }

    fun isTimeValid(time: String): Boolean {
        return Pattern.compile(
            "([01]?[0-9]|2[0-3]):[0-5][0-9]"
        ).matcher(time).matches()
    }

    fun isOnlineValid(online: String): Boolean{
        val onlineLower: String = online.lowercase()
        if (onlineLower == "true" || onlineLower == "false")
            return true
        return false
    }

}