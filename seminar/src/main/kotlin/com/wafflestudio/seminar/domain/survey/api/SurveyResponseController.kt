package com.wafflestudio.seminar.domain.survey.api

import com.wafflestudio.seminar.domain.survey.dto.SurveyResponseDto
import com.wafflestudio.seminar.domain.os.exception.OsNotFoundException
import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.os.service.OperatingSystemService
import com.wafflestudio.seminar.domain.survey.exception.SurveyNotFoundException
import com.wafflestudio.seminar.domain.survey.model.SurveyResponse
import com.wafflestudio.seminar.domain.survey.repository.SurveyResponseRepository
import com.wafflestudio.seminar.domain.survey.service.SurveyResponseService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/results")
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val operatingSystemService: OperatingSystemService,
    private val userService: UserService,
    private val modelMapper: ModelMapper
) {
    @GetMapping("/")
    fun getSurveyResponses(@RequestParam(required = false) os: String?): ResponseEntity<List<SurveyResponseDto.Response>> {
        return try {
            val surveyResponses =
                if (os != null) surveyResponseService.getSurveyResponsesByOsName(os)
                else surveyResponseService.getAllSurveyResponses()
            val responseBody = surveyResponses.map { modelMapper.map(it, SurveyResponseDto.Response::class.java) }
            ResponseEntity.ok(responseBody)
        } catch (e: OsNotFoundException) {
            ResponseEntity.notFound().build()
        }
        // AOP를 적용해 exception handling을 따로 하도록 고쳐보셔도 됩니다.
    }

    @GetMapping("/{id}/")
    fun getSurveyResponse(@PathVariable("id") id: Long): ResponseEntity<SurveyResponseDto.Response> {
        return try {
            val surveyResponse = surveyResponseService.getSurveyResponseById(id)
            val responseBody = modelMapper.map(surveyResponse, SurveyResponseDto.Response::class.java)
            ResponseEntity.ok(responseBody)
        } catch (e: SurveyNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addSurveyResponse(
        @RequestBody @Valid body: SurveyResponseDto.CreateRequest,
        @RequestHeader("User-Id") userId: Long
    ): ResponseEntity<SurveyResponseDto.Response> {
        return try {
            val os = operatingSystemService.getOperatingSystemByName(body.os)
            val user = userService.getUserById(userId)
            val newSurveyResponse = SurveyResponse(
                id = body.id, os = os, springExp = body.spring_exp,
                rdbExp = body.rdb_exp, programmingExp = body.programming_exp,
                major = body.major, grade = body.grade, backendReason = body.backendReason,
                waffleReason = body.waffleReason, somethingToSay = body.somethingToSay,
                timestamp = body.timestamp, user_id = user)
            surveyResponseRepository.save(newSurveyResponse)

            val newDtoResponse = modelMapper.map(body, SurveyResponseDto.Response::class.java)
            ResponseEntity.status(HttpStatus.CREATED).body(newDtoResponse)
        } catch (e: DataIntegrityViolationException){
            ResponseEntity.badRequest().build()
        } catch (e: OsNotFoundException){
            ResponseEntity.notFound().build()
        }
    }

}
