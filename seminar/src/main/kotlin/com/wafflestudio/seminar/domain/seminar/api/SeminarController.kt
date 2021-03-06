package com.wafflestudio.seminar.domain.seminar.api

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.service.SeminarService
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.global.auth.CurrentUser
import com.wafflestudio.seminar.global.common.dto.ListResponse
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
                    @RequestBody(required = false) editRequest: SeminarDto.CreateRequest?): SeminarDto.Response{
        val seminar = seminarService.editSeminar(user, seminarId, editRequest)
        return SeminarDto.Response(seminar)
    }

    @GetMapping("/{seminar_id}/")
    @ResponseStatus(HttpStatus.OK)
    fun getSeminar(@PathVariable("seminar_id") seminarId: Long): SeminarDto.Response{
        val seminar = seminarService.getSeminar(seminarId)
        return SeminarDto.Response(seminar)
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getSeminarList(@RequestParam(required = false) name: String?,
                       @RequestParam(required = false) order: String?
    ): ListResponse<SeminarDto.ListResponse>{
        if(name == null && order == null){
            val seminarList = seminarService.getSeminarList()
            return ListResponse(seminarList?.map{ SeminarDto.ListResponse(it)})
        }
        else{
            val seminarList = seminarService.getSeminarListByNameOrOrder(name, order)
            return ListResponse(seminarList?.map{ SeminarDto.ListResponse(it)})
        }
    }

    @PostMapping("/{seminar_id}/user/")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerSeminar(@PathVariable("seminar_id") seminarId: Long,
                        @CurrentUser user: User,
                        @RequestBody registerRequest: SeminarDto.RegisterRequest)
    : SeminarDto.Response{
        val seminar = seminarService.registerSeminar(seminarId, user, registerRequest)
        return SeminarDto.Response(seminar)
    }

    @DeleteMapping("/{seminar_id}/user/me/")
    @ResponseStatus(HttpStatus.OK)
    fun quitSeminar(@PathVariable("seminar_id") seminarId: Long,
                    @CurrentUser user: User): SeminarDto.Response{
        val seminar = seminarService.quitSeminar(seminarId, user)
        return SeminarDto.Response(seminar)
    }

    //

}