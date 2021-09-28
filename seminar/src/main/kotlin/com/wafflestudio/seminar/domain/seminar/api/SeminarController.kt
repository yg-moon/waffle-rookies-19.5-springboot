package com.wafflestudio.seminar.domain.seminar.api

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.service.SeminarService
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.global.auth.CurrentUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/seminars")
class SeminarController(
    private val seminarService: SeminarService
) {
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSeminar(@CurrentUser user: User,
                      @Valid @RequestBody createRequest: SeminarDto.CreateRequest)
    : SeminarDto.Response{
        val seminar = seminarService.createSeminar(user, createRequest)
        return SeminarDto.Response(seminar)
    }


    @PutMapping("/{seminar_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun editSeminar(@PathVariable("seminar_id") seminarId: Long,
                    @CurrentUser user: User,
                    @RequestBody editRequest: SeminarDto.CreateRequest): SeminarDto.Response{
        val seminar = seminarService.editSeminar(user, seminarId, editRequest)
        return SeminarDto.Response(seminar)
    }

    @GetMapping("/{seminar_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getSeminar(@PathVariable("seminar_id") seminarId: Long): SeminarDto.Response{
        val seminar = seminarService.getSeminar(seminarId)
        return SeminarDto.Response(seminar)
    }





}