package com.absheikh.utils

import io.ktor.server.auth.*


data class UserIDPrincipal(val userId: Long) : Principal