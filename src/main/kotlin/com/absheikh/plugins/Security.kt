package com.absheikh.plugins

import com.absheikh.utils.BaseResponse
import com.absheikh.utils.JWTConfig
import com.absheikh.utils.UserIDPrincipal
import io.ktor.server.auth.*
import io.ktor.util.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureSecurity() {

    install(Authentication){
        val secret: String = "b3bb87b9187e4ff6a721c24fe79e2147"
        jwt("jwt") {
            verifier(JWTConfig.getInstance(secret).getVerifier())

            validate {
                val payload = it.payload
                val userId = payload.getClaim("userID").asLong()

                if (userId != null) {
                    UserIDPrincipal(userId)
                } else {
                    null
                }
            }


        }
    }

}
