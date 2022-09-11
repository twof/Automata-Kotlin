package com.twof.automata.JsonRpc.commands

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class SightSubscription(
    @Serializable(with = UUIDSerializer::class)
    val automatonId: UUID
)
