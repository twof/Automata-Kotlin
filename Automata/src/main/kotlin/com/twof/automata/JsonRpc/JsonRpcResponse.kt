package com.twof.automata.JsonRpc

import kotlinx.serialization.Serializable

@Serializable
data class JsonRpcResponse<Result>(val result: Result, val id: String) {
    val jsonrpc = "2.0"
}