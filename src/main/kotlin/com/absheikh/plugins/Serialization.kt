package com.absheikh.plugins

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.http.*

import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.jackson.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
       jackson(){
           enable(SerializationFeature.INDENT_OUTPUT)
       }

    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}
