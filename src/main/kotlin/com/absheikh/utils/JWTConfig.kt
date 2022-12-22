package com.absheikh.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Date

class JWTConfig private constructor(secret: String){

    private val config: Config = ConfigFactory.load()
    private val audience: String = config.getString("jwt.audience")
    private val issuer: String = config.getString("jwt.issuer")
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)


    fun generateToken(userId: Long): String = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("userID", userId)
        .withExpiresAt(Date(System.currentTimeMillis() + 18000000))
        .sign(algorithm)

    fun getVerifier(): JWTVerifier = JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()



    companion object {
        private var instance: JWTConfig? = null

        fun getInstance(secret: String): JWTConfig {
            if (instance == null) {
                instance = JWTConfig(secret)
            }
            return instance!!
        }
    }

}