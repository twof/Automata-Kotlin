package com.twof.automata.JsonRpc.subscriptionresponses

import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionResponse<Content>(
    val topic: String,
    val content: Content
)
