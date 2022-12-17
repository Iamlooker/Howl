package com.looker.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.looker.core.ui.R

val Poppin = FontFamily(
	Font(R.font.poppins_regular),
	Font(R.font.poppins_medium, FontWeight.Medium),
	Font(R.font.poppins_medium_italic, FontWeight.Medium, FontStyle.Italic),
	Font(R.font.poppins_semi_bold, FontWeight.SemiBold),
	Font(R.font.poppins_semi_bold_italic, FontWeight.SemiBold, FontStyle.Italic),
)

val HowlTypography = Typography(
	h1 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.SemiBold,
		fontStyle = FontStyle.Italic,
		fontSize = 34.sp,
		letterSpacing = (-1).sp
	),
	h2 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontStyle = FontStyle.Italic,
		fontSize = 30.sp,
		letterSpacing = (-0.5).sp
	),
	h3 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 24.sp,
		letterSpacing = 0.sp
	),
	h4 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.SemiBold,
		fontStyle = FontStyle.Italic,
		fontSize = 18.sp,
		letterSpacing = 0.25.sp
	),
	h5 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		letterSpacing = 0.5.sp
	),
	h6 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontStyle = FontStyle.Italic,
		fontSize = 14.sp,
		letterSpacing = 0.15.sp
	),
	subtitle1 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		letterSpacing = 0.15.sp
	),
	subtitle2 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 12.sp,
		letterSpacing = 0.1.sp
	),
	body1 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		letterSpacing = 0.5.sp
	),
	body2 = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		letterSpacing = 0.25.sp
	),
	button = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		letterSpacing = 1.25.sp
	),
	caption = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		letterSpacing = 0.4.sp
	),
	overline = TextStyle(
		fontFamily = Poppin,
		fontWeight = FontWeight.Normal,
		fontSize = 10.sp,
		letterSpacing = 1.5.sp
	)
)
