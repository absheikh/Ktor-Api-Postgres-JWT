package com.absheikh.routes

import com.absheikh.core.db
import com.absheikh.models.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import com.absheikh.utils.BaseResponse
import io.ktor.http.*


fun Route.authRouting() {

    route("/auth") {
        get{
            call.respondText("This is the auth route. The endpoint for registering a user is /auth/register and the endpoint for logging in is /auth/login")
        }
        //Create account
        post("/register"){

            //Get the user from the json request

            val user = call.receive<RegisterParams>()


            //register the user
            val register = User.register(user)

            //Respond with the user in a serialized format
            call.respond(register)
        }
        //Login
        post("/login"){
            val user = call.receive<LoginParams>()
            val login = User.login(user)
            call.respond(login)
        }
    }
}