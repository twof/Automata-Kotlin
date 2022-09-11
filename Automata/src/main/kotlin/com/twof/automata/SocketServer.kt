package com.twof.automata

import com.twof.automata.JsonRpc.JsonRpcErasedRequest
import com.twof.automata.JsonRpc.JsonRpcNotification
import com.twof.automata.JsonRpc.JsonRpcRequest
import com.twof.automata.JsonRpc.JsonRpcResponse
import com.twof.automata.JsonRpc.commands.JumpCommand
import com.twof.automata.JsonRpc.commands.SightSubscription
import com.twof.automata.JsonRpc.commands.SubscribeCommand
import com.twof.automata.JsonRpc.subscriptionresponses.Coordinate3d
import com.twof.automata.JsonRpc.subscriptionresponses.SightSubscriptionResponse
import com.twof.automata.JsonRpc.subscriptionresponses.SubscriptionResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import net.minecraft.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import net.minecraft.block.entity.BlockEntityType

class AutomataManager {
    val automataMap: MutableMap<UUID, AutomataEntity> = mutableMapOf()

    fun insert(automata: AutomataEntity) {
        automataMap[automata.uuid()] = automata
    }
}
class Connection(val session: DefaultWebSocketSession) {
    companion object {
        val lastId = AtomicInteger(0)
    }

    val name = "user${lastId.getAndIncrement()}"
}
fun startSocketServer(world: World) {
    embeddedServer(Netty, port = 8080) {
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
            masking = false
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        routing {
            val automataManager = AutomataManager()
            val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())
            get("/") {
                call.respondText("Hello World!")
            }

            webSocket("/ws") {
                val thisConnection = Connection(this)
                connections += thisConnection

                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val json = Json.decodeFromString(JsonRpcErasedRequest.serializer(), receivedText)
                    route(
                        frame = receivedText,
                        method = json.method,
                        session = this,
                        automataManager = automataManager
                    )
//                    if (receivedText.equals("bye", ignoreCase = true)) {
//                        close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
//                    } else {
//                        send(Frame.Text("Hi, $receivedText!"))
//                    }
                }
            }
        }
    }
    // We don't want to block the main thread, but the game itself will keep it running so this is fine
    .start(wait = false)
}

suspend fun route(frame: String, method: String, session: DefaultWebSocketServerSession, automataManager: AutomataManager) {
    when (method) {
        "jump" -> {
            val request = Json.decodeFromString(JsonRpcRequest.serializer(JumpCommand.serializer()), frame)
            request.params?.automatonId.also {
                automataManager.automataMap[it]?.jump()
            }
            val successResponse = successResponse(request.id)
            val successJson = Json.encodeToString(successResponse)
            session.send(Frame.Text(successJson))
        }

        "subscribe" -> {
            val request = Json.decodeFromString(
                JsonRpcRequest.serializer(SubscribeCommand.serializer(SightSubscription.serializer())),
                frame
            )
            request.params?.params?.automatonId.also {
                automataManager.automataMap[it]?.localVisableBlocks(30.0)?.also {
                    val successResponse = successResponse(request.id)
                    val successJson = Json.encodeToString(successResponse)
                    session.send(Frame.Text(successJson))

                    val sightResponse = JsonRpcNotification("subscribe", SubscriptionResponse(
                        topic = "sight",
                        content = it.map {
                            SightSubscriptionResponse(
                                coordinate = Coordinate3d(
                                    x = it.pos.x,
                                    y = it.pos.y,
                                    z = it.pos.z
                                ),
                                type = BlockEntityType.getId(it.type)!!.namespace
                            )
                        }
                    ))
                    val visibleBlocks = Json.encodeToString(sightResponse)
                    session.send(Frame.Text(visibleBlocks))
                }
            }
        }
    }
}

fun successResponse(id: String): JsonRpcResponse<String> {
    JsonRpcResponse(
        result = "OK",
        id = id
    )
}