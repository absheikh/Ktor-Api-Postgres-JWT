package com.absheikh.utils

import io.ktor.http.*


sealed class  BaseResponse<T> (val statusCode: Any) {

    data class  Success<T>(
        val data : T? = null,
        val message: String? = null
    ) : BaseResponse<T>(HttpStatusCode.OK)

    data class  Duplicate<T>(
        val exception : T? =null,
        val message: String? = null,

    ) : BaseResponse<T>(HttpStatusCode.Conflict)

    data class UnAuthorized<T>(
        val exception : T? =null,
        val message: String? = null,

    ) : BaseResponse<T>(HttpStatusCode.Unauthorized)

    data class  Error<T>(
        val exception : T? =null,
        val message: String? = null,

    ) : BaseResponse<T>(HttpStatusCode.BadRequest)

}