package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.*
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.exception.InvalidRoleException
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
) {
    fun createSeminar(user: User, createRequest: SeminarDto.CreateRequest): Seminar{
        if (!isOnlineValid(createRequest.online)) throw InvalidFormException("Invalid form")
        if (user.instructorProfile == null) throw NotInstructorException("Not an instructor")

        var online = true
        val toLower = createRequest.online.lowercase()
        if (toLower == "false") online = false

        val tempSeminar = Seminar(
                createRequest.name,
                createRequest.capacity,
                createRequest.count,
                createRequest.time,
                online,
            )

        val foundUser = userRepository.findByIdOrNull(user.id)
        tempSeminar.seminarInstructors?.add(foundUser?.instructorProfile)
        foundUser?.instructorProfile?.seminar = tempSeminar

        return seminarRepository.save(tempSeminar)
    }

    private fun isOnlineValid(online: String): Boolean{
        val onlineLower: String = online.lowercase()
        if (onlineLower == "true" || onlineLower == "false")
            return true
        return false
    }

    fun editSeminar(user: User, seminarId: Long, editRequest: SeminarDto.CreateRequest?): Seminar{
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("Seminar not found")

        // Check Exceptions
        if(editRequest == null) return seminar
        editSeminarCheckExceptions(user, seminar, editRequest)

        // Assign variables
        val name = if (editRequest.name == "") seminar.name else editRequest.name
        val capacity = if (editRequest.capacity == 0) seminar.capacity else editRequest.capacity
        val count = if (editRequest.count == 0) seminar.count else editRequest.count
        val time = if (editRequest.time == "") seminar.time else editRequest.time
        val online = editRequest.time == "true"
        seminar.name = name
        seminar.capacity = capacity
        seminar.count = count
        seminar.time = time
        seminar.online = online

        return seminarRepository.save(seminar)
    }

    private fun editSeminarCheckExceptions(user: User, seminar: Seminar, editRequest: SeminarDto.CreateRequest){
        if (seminar.seminarInstructors?.isEmpty() == true) throw NotInChargeException("Not in charge")
        val instructorPresent = seminar.seminarInstructors?.any{
            it?.id == userRepository.findByIdOrNull(user.id)?.instructorProfile?.id
        }
        if (instructorPresent == false) throw NotInChargeException("Not in charge")

        val currentCount = seminar.seminarParticipants?.count{
            it?.isActive == true
        }
        if (editRequest.capacity < currentCount!!) throw InvalidFormException("Invalid capacity")
    }

    fun getSeminar(seminarId: Long): Seminar {
        return seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("Seminar not found")
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
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("Seminar not found")
        val role = registerRequest.role
        val foundUser = userRepository.findByIdOrNull(user.id) ?: throw UserNotFoundException("User not found")

        // Check Exceptions
        registerSeminarCheckExceptions(foundUser, seminar, role)

        // Actual Registration
        val updatedSeminar = seminar
        if (role == "participant"){
            val seminarParticipant = SeminarParticipant(
                LocalDateTime.now(),
                true,
                null,
                seminar,
                foundUser.participantProfile,
            )
            updatedSeminar.seminarParticipants?.add(seminarParticipant)
        }
        if (role == "instructor"){
            val instructorProfile = InstructorProfile(
                "",
                null,
                foundUser,
                seminar
            )
            updatedSeminar.seminarInstructors?.add(instructorProfile)
        }

        return seminarRepository.save(updatedSeminar)
    }

    private fun registerSeminarCheckExceptions(user: User, seminar: Seminar, role: String){
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
            it?.participantProfile?.user?.id == user.id
        }
        val asInstructor = seminar.seminarInstructors?.any{
            it?.user?.id == user.id
        }
        if (asParticipant == true || asInstructor == true)
            throw AlreadyRegisteredException("Already registered")
    }

    fun quitSeminar(seminarId: Long, user: User): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("Seminar not found2")
        val foundUser = userRepository.findByIdOrNull(user.id) ?: throw UserNotFoundException("User not found")

        // Exception checks
        val isInCharge = seminar.seminarInstructors?.any{
            it?.id == foundUser.id
        }
        if (isInCharge == true) throw AlreadyInChargeException("In charge of this seminar")

        // Actual logic
        val isParticipating = seminar.seminarParticipants?.any{
            it?.participantProfile?.user?.id == foundUser.id && it.participantProfile!!.accepted
        }
        if (isParticipating == true){
            val seminarParticipant = seminar.seminarParticipants?.find { it?.participantProfile?.user?.id == foundUser.id }
            seminarParticipant?.isActive = false
            seminarParticipant?.droppedAt = LocalDateTime.now()
        }

        return seminarRepository.save(seminar)
    }


}