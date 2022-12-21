package com.absheikh.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


fun Route.userRouting() {

    route("/users") {
        get{
            call.respondText("Hello World!")
        }
        //Create account
        post("/register"){
            call.respondText("Hello World!")
        }
    }
}