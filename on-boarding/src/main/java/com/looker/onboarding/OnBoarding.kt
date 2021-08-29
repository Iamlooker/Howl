package com.looker.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.looker.constants.Constants.fadeInDuration
import com.looker.onboarding.components.ButtonWithIcon
import com.looker.onboarding.utils.handlePermissions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnBoardingPage(
    viewModel: OnBoardingViewModel = viewModel(),
    navigate: () -> Unit,
) {

    val color = viewModel.buttonColor
    val buttonText = viewModel.buttonText
    val buttonIcon = viewModel.buttonIcon

    val bannerText = AnnotatedString(
        "Whoops",
        SpanStyle(
            MaterialTheme.colors.onBackground,
            fontSize = 24.sp
        )
    ) + AnnotatedString(
        " Nothing ",
        SpanStyle(
            color = color,
            fontSize = 24.sp
        )
    ) + AnnotatedString(
        "Here",
        SpanStyle(
            MaterialTheme.colors.onBackground,
            fontSize = 24.sp
        )
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                viewModel.onPermissionGranted()
            } else {
                viewModel.onPermissionDenied()
            }
        }
    )

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    OnBoardImage(
        bannerText = bannerText,
        buttonText = buttonText,
        buttonIcon = buttonIcon,
        buttonColor = color
    ) {
        handlePermissions(
            context,
            permissionLauncher,
            onGranted = {
                viewModel.onPermissionGranted()
                scope.launch {
                    delay(fadeInDuration.toLong())
                    navigate()
                }
            },
            onDenied = { viewModel.onPermissionDenied() }
        )
    }

}

@Composable
fun OnBoardImage(
    bannerText: AnnotatedString,
    buttonText: String,
    buttonIcon: ImageVector,
    buttonColor: Color,
    onClick: () -> Unit,
) {
    OnBoardContent(
        bannerText = bannerText,
        buttonText = buttonText,
        buttonIcon = buttonIcon,
        buttonColor = buttonColor,
        painter = painterResource(id = R.drawable.empty),
        onClick = onClick
    )
}

@Composable
fun OnBoardContent(
    bannerText: AnnotatedString,
    buttonText: String,
    buttonIcon: ImageVector,
    buttonColor: Color,
    painter: Painter,
    onClick: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painter,
                contentDescription = null
            )

            Text(
                text = bannerText,
                textAlign = TextAlign.Center
            )

            ButtonWithIcon(
                buttonText = buttonText,
                buttonIcon = buttonIcon,
                buttonColor = buttonColor,
                onClick = onClick
            )
        }
    }
}