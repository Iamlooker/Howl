package com.looker.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {

    private val orange = Color(0xFFFF9e80)
    private val green = Color(0xFF69f0ae)

    private val _buttonText = MutableLiveData<String>()
    private val _buttonIcon = MutableLiveData<ImageVector>()
    private val _buttonColor = MutableLiveData<Color>()

    val buttonText = _buttonText
    val buttonIcon = _buttonIcon
    val buttonColor = _buttonColor

    fun onPermissionGranted() {
        _buttonText.value = "Granted"
        _buttonIcon.value = Icons.Rounded.DoneAll
        _buttonColor.value = green
    }

    fun onPermissionDenied() {
        _buttonText.value = "Denied"
        _buttonIcon.value = Icons.Rounded.Close
        _buttonColor.value = orange
    }

    fun bannerText(onBackground: Color) = buildAnnotatedString {

        withStyle(
            style = SpanStyle(
                color = onBackground,
                fontSize = 24.sp
            )
        ) {
            append("Whoops")
        }
        withStyle(
            style = SpanStyle(
                color = buttonColor.value ?: orange,
                fontSize = 24.sp
            )
        ) {
            append(" Nothing ")
        }
        withStyle(
            style = SpanStyle(
                color = onBackground,
                fontSize = 24.sp
            )
        ) {
            append("Here")
        }
    }
}