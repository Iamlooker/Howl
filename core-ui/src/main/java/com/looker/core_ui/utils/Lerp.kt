package com.looker.core_ui.utils

import androidx.annotation.FloatRange

/*
	https://github.com/android/compose-samples/blob/e6f9d376d435ae8c94f9caff0eaa7a0befccb3a0/Owl/app/src/main/java/com/example/owl/ui/utils/Lerp.kt#L26
 */

fun lerp(
	startValue: Float,
	endValue: Float,
	@FloatRange(from = 0.0, to = 1.0) fraction: Float
): Float {
	return startValue + fraction * (endValue - startValue)
}