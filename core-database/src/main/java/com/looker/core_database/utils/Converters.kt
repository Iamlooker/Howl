package com.looker.core_database.utils

import androidx.room.TypeConverter

const val SET_DELIMITER = "!@#$%^&*"

class InstantConverters {
	@TypeConverter
	fun stringToSet(value: String): Set<String> = value.split(SET_DELIMITER).toSet()

	@TypeConverter
	fun setToString(value: Set<String>): String = value.joinToString(SET_DELIMITER)
}