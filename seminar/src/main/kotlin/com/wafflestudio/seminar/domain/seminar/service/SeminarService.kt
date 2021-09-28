package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.InvalidFormException
import com.wafflestudio.seminar.domain.seminar.exception.NotInChargeException
import com.wafflestudio.seminar.domain.seminar.exception.NotInstructorException
import com.wafflestudio.seminar.domain.seminar.exception.SeminarNotFoundException
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.dto.UserDto
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
            !isOnlineValid(createRequest.online)) throw InvalidFormException("Invalid form")

        if (user.roles != "instructor") throw NotInstructorException("Not an instructor")

        val toLower = createRequest.online.lowercase()
        var online = true
        if (toLower == "false") online = false

        val save = seminarRepository.save(
            Seminar(
                createRequest.name,
                createRequest.capacity,
                createRequest.count,
                createRequest.time,
                online,
            )
        )
        val newSeminar = save

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

    fun editSeminar(user: User, seminarId: Long, editRequest: SeminarDto.CreateRequest): Seminar{
        val optSeminar = seminarRepository.findById(seminarId)
        if (optSeminar.isEmpty) throw SeminarNotFoundException("Seminar not found")

        val seminar = optSeminar.get()

        val instructorPresent = seminar.seminarInstructors?.any{
            it.user?.id == user.id
        }
        if (instructorPresent == false) throw NotInChargeException("Not in charge")

        val currentCount = seminar.seminarParticipants?.count{
            it.isActive == true
        }
        if (editRequest.capacity < currentCount!!) throw InvalidFormException("Invalid capacity")

        val name = if (editRequest.name == "") seminar.name else editRequest.name
        val capacity = if (editRequest.capacity == 0) seminar.capacity else editRequest.capacity
        val count = if (editRequest.count == 0) seminar.count else editRequest.count
        val time = if (editRequest.time == "") seminar.time else editRequest.time
        val online = if (editRequest.time == "true") true else false

        return seminarRepository.save(Seminar(
            name,
            capacity,
            count,
            time,
            online,
        ))







    }

}