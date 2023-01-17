package com.looker.onboarding

import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.looker.onboarding.utils.PermissionButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OnBoardingViewModel : ViewModel() {

	private val _buttonState = MutableStateFlow<PermissionButtonState>(PermissionButtonState.WaitingAction)
	val buttonState = _buttonState.asStateFlow()

	fun onPermissionGranted() {
		viewModelScope.launch { _buttonState.emit(PermissionButtonState.GRANTED) }
	}

	fun onPermissionDenied() {
		viewModelScope.launch { _buttonState.emit(PermissionButtonState.DENIED) }
	}

	val bannerText = buildAnnotatedString {

		withStyle(style = SpanStyle()) {
			append("Whoops")
		}
		withStyle(
			style = SpanStyle(color = buttonState.value.color)
		) {
			append(" Nothing ")
		}
		withStyle(style = SpanStyle()) {
			append("Here")
		}
	}
}