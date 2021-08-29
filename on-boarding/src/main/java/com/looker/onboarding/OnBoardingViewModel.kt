package com.looker.onboarding

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class OnBoardingViewModel : ViewModel() {

    private val orange = Color(0xFFFF9e80)
    private val green = Color(0xFF69f0ae)

    var buttonText by mutableStateOf("Grant Permission")

    var buttonIcon by mutableStateOf(Icons.Default.Close)

    var buttonColor by mutableStateOf(orange)

    fun onPermissionGranted() {
        buttonText = "Granted"
        buttonIcon = Icons.Default.DoneAll
        buttonColor = green
    }

    fun onPermissionDenied() {
        buttonText = "Denied"
        buttonIcon = Icons.Default.Cancel
        buttonColor = orange
    }
}