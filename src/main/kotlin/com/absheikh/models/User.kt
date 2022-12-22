package com.absheikh.models


import com.absheikh.core.db
import com.absheikh.utils.BaseResponse
import io.ktor.http.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.Entity
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import org.mindrot.jbcrypt.BCrypt


interface User : Entity<User> {
    companion object : Entity.Factory<User>(){

        fun register(params: RegisterParams) : BaseResponse<Any>{


            //if the user already exists
            if(emailExists(params.email)){

                return BaseResponse.Duplicate( message = "User already exists")
            }

            //Check if params are not empty
            if(params.email.isEmpty() || params.password.isEmpty() || params.name.isEmpty()){
                return BaseResponse.Error(message = "All fields are required")
            }

            //hash password
            val hashedPassword = BCrypt.hashpw(params.password, BCrypt.gensalt())
            //save user to database
             val num = db.insert(Users)  {
                set(it.name, params.name)
                set(it.email, params.email)
                set(it.password, hashedPassword)
                set(it.role, params.role)

            }
            return if (num > 0){
                var user = db.users.find { Users.email eq params.email }

                var userData = user?.let {
                    UserResponse (
                        id = it.id,
                        name = user.name,
                        email = user.email,
                        role = user.role,
                        createdAt = user.createdAt.toString(),
                        updatedAt = user.updatedAt.toString()
                    )
                }

                BaseResponse.Success(data = userData, message = "Registered successfully")


            } else{
                BaseResponse.Error(message = "Something went wrong")
            }




        }
        fun login(params: LoginParams): BaseResponse<Any> {
            val user = db.users.find { it.email eq params.email }
            if(user != null){
                if(BCrypt.checkpw(params.password, user.password)){
                    var userData = user.let {
                        UserResponse (
                            id = it.id,
                            name = user.name,
                            email = user.email,
                            role = user.role,
                            createdAt = user.createdAt.toString(),
                            updatedAt = user.updatedAt.toString()
                        )
                    }
                    return BaseResponse.Success(data = userData, message = "Logged in successfully")
                }
            }
            return BaseResponse.Error(message = "Invalid credentials")
        }

        fun emailExists(email: String): Boolean {
            val user = db.users.find { it.email.eq(email)}
            return user != null
        }


    }
    var id: Long
    var name: String
    var email: String
    var password: String
    var role: Int
    var createdAt: LocalDateTime
    var updatedAt: LocalDateTime

}


object Users : Table<User>("users") {
    val id = long("id").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val email = varchar("email").bindTo { it.email }
    val password = varchar("password").bindTo { it.password }
    val role = int("role").bindTo { it.role }
    val createdAt = datetime("created_at").bindTo { it.createdAt }
    val updatedAt = datetime("updated_at").bindTo { it.updatedAt }
}


val Database.users get() = this.sequenceOf(Users)

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: Int,
    val token: String? = null,
    val createdAt: String,
    val updatedAt: String

)

data class RegisterParams(
    val name: String,
    val email: String,
    val role: Int? = 2,
    val password: String
)

data class LoginParams(
    val email: String,
    val password: String
)
