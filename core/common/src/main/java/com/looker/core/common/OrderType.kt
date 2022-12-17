package com.looker.core.common

sealed class OrderType {
	object Ascending : OrderType()
	object Descending : OrderType()
}