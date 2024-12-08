package com.fsacchi.schoolmate.data.local.extensions

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun <T> Any.exec(block: suspend FlowCollector<T>.() -> T): Flow<T> = flow {
    try {
        emit(block())
    } catch (e: Exception) {
        throw e
    }
}.flowOn(Dispatchers.IO)
