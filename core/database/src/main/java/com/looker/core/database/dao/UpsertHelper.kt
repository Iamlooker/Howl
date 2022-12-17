package com.looker.core.database.dao

suspend fun <T> upsert(
	items: List<T>,
	insertMany: suspend (List<T>) -> List<Long>,
	updateMany: suspend (List<T>) -> Unit,
) {
	val insertResults = insertMany(items)

	val updateList = items.zip(insertResults)
		.mapNotNull { (item, insertResult) ->
			if (insertResult == -1L) item else null
		}
	if (updateList.isNotEmpty()) updateMany(updateList)
}