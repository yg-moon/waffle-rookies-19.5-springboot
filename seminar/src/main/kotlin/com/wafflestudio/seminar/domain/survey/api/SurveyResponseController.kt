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

    // Q8. POST /api/v1/results/ (설문조사 생성)
    @PostMapping("/")
    fun addSurveyResponse(
        @RequestBody @Valid body: SurveyResponseDto.CreateRequest,
        @RequestHeader("User-Id") userId: Long
    ): ResponseEntity<SurveyResponseDto.Response> {
        return try {
            // 설문내용을 받아서 DB에 저장
            val newSurveyResponse = modelMapper.map(body, SurveyResponse::class.java)
            newSurveyResponse.os = operatingSystemService.getOperatingSystemByName(body.os)
            newSurveyResponse.user = userService.getUserById(userId)
            surveyResponseRepository.save(newSurveyResponse)

            // Response 형식에 맞춰 리턴
            val newDtoResponse = modelMapper.map(body, SurveyResponseDto.Response::class.java)
            ResponseEntity
                .status(HttpStatus.CREATED)
                .body(newDtoResponse)
        } catch (e: DataIntegrityViolationException){
            ResponseEntity.badRequest().build() // 400
        } catch (e: OsNotFoundException){
            ResponseEntity.notFound().build() // 404
        }
    }

}
