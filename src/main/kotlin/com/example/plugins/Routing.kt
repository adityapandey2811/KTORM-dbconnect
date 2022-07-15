package com.example.plugins

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import com.example.routes.connectToDB

fun Application.configureRouting() {

//    routing {
//        get("/") {
//            call.respondText("Hello World!")
//        }
//    }
    connectToDB()
}
