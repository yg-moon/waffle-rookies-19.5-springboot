package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service


@Service
class UserService(
    private val userRepository: UserRepository,
) {
    fun getUserById(id: Long): User{
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    }

}

//    fun getAllSurveyResponses(): List<SurveyResponse> {
//        return surveyResponseRepository.findAll()
//    }
//
//    fun getSurveyResponsesByOsName(name: String): List<SurveyResponse> {
//        val os = operatingSystemRepository.findByNameEquals(name) ?: throw OsNotFoundException()
//        return surveyResponseRepository.findAllByOs(os)
//    }
//
//    fun getSurveyResponseById(id: Long): SurveyResponse? {
//        return surveyResponseRepository.findByIdOrNull(id) ?: throw SurveyNotFoundException()
//    }
