package com.twof.automata.JsonRpc.commands

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SubscribeCommand<Params>(
    val topic: String,
    val params: Params
)