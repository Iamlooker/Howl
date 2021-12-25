package com.looker.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel : ViewModel() {

	private val orange = Color(0xFFFF9e80)
	private val green = Color(0xFF69f0ae)

	private val _buttonText = MutableStateFlow("Grant Permission")
	private val _buttonIcon = MutableStateFlow(Icons.Rounded.Close)
	private val _buttonColor = MutableStateFlow(orange)

	val buttonText = _buttonText.asStateFlow()
	val buttonIcon = _buttonIcon.asStateFlow()
	val buttonColor = _buttonColor.asStateFlow()

	fun onPermissionGranted() {
		viewModelScope.launch {
			_buttonText.emit("Granted")
			_buttonIcon.emit(Icons.Rounded.DoneAll)
			_buttonColor.emit(green)
		}
	}

	fun onPermissionDenied() {
		viewModelScope.launch {
			_buttonText.emit("Denied")
			_buttonIcon.emit(Icons.Rounded.Close)
			_buttonColor.emit(orange)
		}
	}

	val bannerText = buildAnnotatedString {

		withStyle(
			style = SpanStyle(fontSize = 24.sp)
		) {
			append("Whoops")
		}
		withStyle(
			style = SpanStyle(
				color = buttonColor.value,
				fontSize = 24.sp
			)
		) {
			append(" Nothing ")
		}
		withStyle(
			style = SpanStyle(fontSize = 24.sp)
		) {
			append("Here")
		}
	}
}