package com.wafflestudio.seminar.domain.seminar.exception

import com.wafflestudio.seminar.global.common.exception.ErrorType
import com.wafflestudio.seminar.global.common.exception.InvalidRequestException

class CapacityLimitException (detail: String="") :
    InvalidRequestException(ErrorType.INVALID_REQUEST, detail)