package com.twof.automata

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080) { 
        routing {
            get("/") {
                call.respondText("Hello World!")
            }
        }
     }.start(wait = true)
}