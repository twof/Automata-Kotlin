package com.twof.automata.JsonRpc

import kotlinx.serialization.Serializable

@Serializable
data class JsonRpcNotification<Params>(val method: String, val params: Params?) {
    val jsonrpc = "2.0"
}