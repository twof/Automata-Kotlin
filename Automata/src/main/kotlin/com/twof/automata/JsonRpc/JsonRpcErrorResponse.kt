package com.twof.automata.JsonRpc

import kotlinx.serialization.Serializable

@Serializable
data class JsonRpcErrorResponse<Error>(val result: Error, val id: String) {
    val jsonrpc = "2.0"
}

@Serializable
data class JsonRpcError<Error>(val result: Error, val id: String) {
    val jsonrpc = "2.0"
}