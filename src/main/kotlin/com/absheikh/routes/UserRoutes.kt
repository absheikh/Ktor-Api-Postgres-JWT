package com.absheikh.routes

import com.absheikh.core.db
import com.absheikh.models.User
import com.absheikh.models.UserData
import com.absheikh.models.UserResponse
import com.absheikh.models.users
import io.ktor.server.application.*
import io.ktor.server.request.*
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

            //Get the user from the json request

            val user = call.receive<UserData>()


            //check if the user already exists
            val existingUser = User.emailExists(user.email)
            if(existingUser){
                call.respondText("User already exists")
                return@post
            }


            //Save the user to the database
            val register = User.register(user.name, user.email, user.password)
            //Respond with the user in a serialized format


            call.respond(UserResponse(register.id, register.name, register.email, register.role))



        }
    }
}