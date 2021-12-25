package com.looker.howlmusic.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val HowlTypography = Typography(
	body1 = TextStyle(
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp
	),
	body2 = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp
	),
	caption = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 13.sp
	),
	h6 = TextStyle(
		fontWeight = FontWeight.SemiBold,
		fontSize = 17.sp
	)
)