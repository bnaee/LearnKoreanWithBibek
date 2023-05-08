package com.genesiswtech.lkwb.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LKWBEventBus {
    private val _eventFlow = MutableSharedFlow<LKWBEvents>()

    fun subscribe(scope: CoroutineScope, block: suspend (LKWBEvents) -> Unit) =
        _eventFlow.onEach(block).launchIn(scope)

    suspend fun emit(appEvent: LKWBEvents) = _eventFlow.emit(appEvent)
}