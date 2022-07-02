package com.looker.core_common

sealed class OrderType {
	object Ascending : OrderType()
	object Descending : OrderType()
}