package com.twof.automata.JsonRpc

import kotlinx.serialization.Serializable

@Serializable
data class JsonRpcRequest<Params>(val method: String, val params: Params?, val id: String) {
    val jsonrpc = "2.0"
}

@Serializable
data class JsonRpcErasedRequest(val method: String)