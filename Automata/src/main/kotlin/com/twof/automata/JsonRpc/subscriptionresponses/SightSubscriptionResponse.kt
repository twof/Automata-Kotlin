package com.twof.automata.JsonRpc.subscriptionresponses

import kotlinx.serialization.Serializable

@Serializable
data class Coordinate3d(
    val x: Int,
    val y: Int,
    val z: Int
)

@Serializable
data class SightSubscriptionResponse(
    val coordinate: Coordinate3d,
    val type: String
)