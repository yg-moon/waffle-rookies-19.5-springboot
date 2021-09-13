package com.wafflestudio.seminar.domain.user.api


import com.wafflestudio.seminar.domain.survey.dto.SurveyResponseDto
import com.wafflestudio.seminar.domain.survey.exception.SurveyNotFoundException
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.domain.user.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val modelMapper: ModelMapper
) {

    // Q6.
    @PostMapping("/")
    fun addUser(
        @RequestBody @Valid body: UserDto.CreateRequest,
    ): ResponseEntity<UserDto.Response>{
        return try{
            val user = User(name = body.name, email = body.email)
            val response = modelMapper.map(body, UserDto.Response::class.java)
            userRepository.save(user)
            return ResponseEntity.ok(response)
        } catch (e: DataIntegrityViolationException){
            ResponseEntity.badRequest().build()
        }
    }

    // Q7.
    @GetMapping("/me/")
    fun getSurveyResponse(@RequestHeader("User-Id") userId: Long): ResponseEntity<UserDto.Response> {
        return try {
            val user = userService.getUserById(userId)
            val responseBody = modelMapper.map(user, UserDto.Response::class.java)
            ResponseEntity.ok(responseBody)
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

}
