package com.looker.core_common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

inline fun <T, R> Flow<T>.mapAndStateIn(
	scope: CoroutineScope,
	initialValue: R,
	whileMillis: Long = 5000,
	crossinline map: suspend (value: T) -> R
): StateFlow<R> = this.map(map).stateIn(
	scope = scope,
	started = SharingStarted.WhileSubscribed(whileMillis),
	initialValue = initialValue
)