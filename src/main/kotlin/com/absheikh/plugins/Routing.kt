package com.absheikh.plugins

import com.absheikh.models.User
import com.absheikh.routes.*
import com.absheikh.utils.UserIDPrincipal
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureRouting() {

    routing {
        authRouting()

        //Dashboard routes
        authenticate("jwt") {
            get("/profile") {
                val user = call.principal<UserIDPrincipal>()
                //Get the user from the database
                val profile = User.getProfile(user!!.userId)
                call.respond(profile)
            }
        }
    }
}
