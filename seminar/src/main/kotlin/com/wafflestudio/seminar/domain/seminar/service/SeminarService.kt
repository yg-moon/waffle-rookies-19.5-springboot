package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.*
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.exception.InvalidRoleException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
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

        val tempSeminar = seminarRepository.save(Seminar(
                createRequest.name,
                createRequest.capacity,
                createRequest.count,
                createRequest.time,
                online,
            ))
        user.instructorProfile?.seminar = tempSeminar
        userRepository.save(user)
        return seminarRepository.save(tempSeminar)
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

        var seminar = optSeminar.get()

        // Various Exceptions
        val instructorPresent = seminar.seminarInstructors?.any{
            it.user?.id == user.id
        }
        if (instructorPresent == false) throw NotInChargeException("Not in charge")

        val currentCount = seminar.seminarParticipants?.count{
            it.isActive == true
        }
        if (editRequest.capacity < currentCount!!) throw InvalidFormException("Invalid capacity")

        // Assign variables
        val name = if (editRequest.name == "") seminar.name else editRequest.name
        val capacity = if (editRequest.capacity == 0) seminar.capacity else editRequest.capacity
        val count = if (editRequest.count == 0) seminar.count else editRequest.count
        val time = if (editRequest.time == "") seminar.time else editRequest.time
        val online = if (editRequest.time == "true") true else false

        seminar.name = name
        seminar.capacity = capacity
        seminar.count = count
        seminar.time = time
        seminar.online = online

        return seminarRepository.save(seminar)
    }

    fun getSeminar(seminarId: Long): Seminar {
        val optSeminar = seminarRepository.findById(seminarId)
        if (optSeminar.isEmpty) throw SeminarNotFoundException("Seminar not found")
        return optSeminar.get()
    }

    fun getSeminarListByNameOrOrder(name: String?, order: String?): List<Seminar>?{
        var list: List<Seminar>? = null

        if(name != null){
            list = seminarRepository.findSeminarsByNameContains(name)
            list = list?.sortedBy { it.createdAt }
            list = list?.reversed()
            if(order != null && order == "earliest"){
                list = list?.reversed()
            }
        }
        else if(order != null && order == "earliest"){
            list = seminarRepository.findAll()
            list = list.sortedBy { it.createdAt }
        }

        return list
    }

    fun getSeminarList(): List<Seminar>?{
        var list = seminarRepository.findAll()
        list = list.sortedBy { it.createdAt }
        list = list.reversed()
        return list
    }

    fun registerSeminar(seminarId: Long, user: User,
                        registerRequest: SeminarDto.RegisterRequest): Seminar{
        val optSeminar = seminarRepository.findById(seminarId)
        if (optSeminar.isEmpty) throw SeminarNotFoundException("Seminar not found")
        val seminar = optSeminar.get()
        val role = registerRequest.role

        // Various Exceptions
        if (!(role == "participant" || role == "instructor"))
            throw InvalidRoleException("Role must be either participant or instructor")
        if (role == "participant"){
            if(user.participantProfile == null)
                throw NotParticipantException("Not a participant")
            if(!user.participantProfile!!.accepted)
                throw NotAcceptedException("Not accepted")
            if(seminar.capacity <= (seminar.seminarParticipants?.count() ?: 0))
                throw CapacityLimitException("Capacity exceeded")
        }
        if (role == "instructor"){
            if(user.instructorProfile == null)
                throw NotInstructorException("Not a instructor")
            if(user.instructorProfile?.seminar != null)
                throw AlreadyInChargeException("Already in charge")
        }

        val hadDropped = user.participantProfile?.seminarParticipants?.any{
            it.droppedAt != null && it.seminar.id == seminar.id
        }
        if (hadDropped == true) throw HadDroppedException("Had dropped")


        // Already Registered
        val asParticipant = seminar.seminarParticipants?.any{
            it.participantProfile.user?.id == user.id
        }
        val asInstructor = seminar.seminarInstructors?.any{
            it.user?.id == user.id
        }
        if (asParticipant == true || asInstructor == true)
            throw AlreadyRegisteredException("Already registered")

        var updatedSeminar = seminar
        // Actual Registration
        if (role == "participant"){
            val participantProfile = ParticipantProfile(
                "",
                true,
                user,
            )
            SeminarParticipant(
                LocalDateTime.now(),
                true,
                null,
                seminar,
                participantProfile
            )
            updatedSeminar = seminarRepository.save(seminar)
        }
        if (role == "instructor"){
            val instructorProfile = InstructorProfile(
                "",
                null,
                user,
                seminar
            )
            updatedSeminar = seminarRepository.save(seminar)
        }

        return updatedSeminar
    }

    fun quitSeminar(seminarId: Long, user: User): Seminar {
        val optSeminar = seminarRepository.findById(seminarId)
        if (optSeminar.isEmpty) throw SeminarNotFoundException("Seminar not found")
        val seminar = optSeminar.get()

        if (user.roles == "instructor") throw NotParticipantException("Not a participant")

        val isParticipating = seminar.seminarParticipants?.any{
            it.participantProfile.user?.id == user.id && it.participantProfile.accepted
        }
        if (isParticipating == true){
            var seminarParticipant = seminar.seminarParticipants?.find { it.participantProfile.user?.id == user.id }
            seminarParticipant?.isActive = false
            seminarParticipant?.droppedAt = LocalDateTime.now()
        }

        return seminarRepository.save(seminar)
    }


}