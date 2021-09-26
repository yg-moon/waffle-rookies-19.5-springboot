package com.wafflestudio.seminar.domain.user.exception

import com.wafflestudio.seminar.global.common.exception.ErrorType
import com.wafflestudio.seminar.global.common.exception.InvalidRequestException

class InvalidYearException (detail: String="") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)