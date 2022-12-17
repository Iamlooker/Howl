package com.looker.core.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.experimental.ExperimentalTypeInference

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

@OptIn(ExperimentalTypeInference::class)
fun <T1, T2, R> combineAndStateIn(
	flow1: Flow<T1>,
	flow2: Flow<T2>,
	scope: CoroutineScope,
	initialValue: R,
	whileMillis: Long = 5000,
	@BuilderInference transform: suspend (T1, T2) -> R
): StateFlow<R> = combine(flow1, flow2, transform).stateIn(
	scope = scope,
	started = SharingStarted.WhileSubscribed(whileMillis),
	initialValue = initialValue
)

@OptIn(ExperimentalTypeInference::class)
fun <T1, T2, T3, R> combineAndStateIn(
	flow1: Flow<T1>,
	flow2: Flow<T2>,
	flow3: Flow<T3>,
	scope: CoroutineScope,
	initialValue: R,
	whileMillis: Long = 5000,
	@BuilderInference transform: suspend (T1, T2, T3) -> R
): StateFlow<R> = combine(flow1, flow2, flow3, transform).stateIn(
	scope = scope,
	started = SharingStarted.WhileSubscribed(whileMillis),
	initialValue = initialValue
)