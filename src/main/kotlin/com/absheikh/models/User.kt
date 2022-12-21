package com.absheikh.models


import com.absheikh.core.db
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

        fun register(name: String, email: String, password: String): User {
            //hash password
            val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
            //save user to database
             db.insert(Users)  {
                set(it.name, name)
                set(it.email, email)
                set(it.password, hashedPassword)
                set(it.role, 1)

            }
            //return the user
            return db.users.find { Users.email eq email }!!

        }
        fun login(email: String, password: String): User? {
            val user = db.users.find { it.email eq email }
            if(user != null){
                if(BCrypt.checkpw(password, user.password)){
                    return user
                }
            }
            return null
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

@Serializable
data class UserData(
    val name: String,
    val email: String,
    val password: String,
    val role: Int,

)
@Serializable
data class UserResponse(val id: Long, val name: String, val email: String, val role: Int)