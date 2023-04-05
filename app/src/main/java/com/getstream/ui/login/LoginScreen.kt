@file:OptIn(ExperimentalMaterialApi::class)

package com.getstream.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ModalBottomSheetValue.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getstream.ui.theme.GetStreamPerusalTheme
import com.getstream.viewmodels.LoginViewModel
import com.google.android.gms.common.SignInButton
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onGoogleSignInClicked: () -> Unit = {},
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val isConnecting by loginViewModel.isConnecting.collectAsState()

    BottomSheetScaffold(
        sheetContent = {
            SheetContent(onGoogleSignInClicked = onGoogleSignInClicked)
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        LayoutContent(onButtonClick = {
            scope.launch { scaffoldState.bottomSheetState.expand() }
        })
    }

    if (isConnecting) {
        CircularIndicatorWithDimmedBackground()
    }
}

@Composable
@Preview
private fun CircularIndicatorWithDimmedBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
            .pointerInput(Unit) { }
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    GetStreamPerusalTheme {
        LoginScreen()
    }
}

@Composable
fun SheetContent(
    onGoogleSignInClicked: () -> Unit = {}
) = Column(
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.8f)
) {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    BottomSheetHandle()

    AndroidView(factory = { SignInButton(it) }) { button ->
        button.setOnClickListener { onGoogleSignInClicked() }
    }
    Text("TODO: BottomSheet Content")
}

@Preview
@Composable
private fun ColumnScope.BottomSheetHandle() = Box(
    modifier = Modifier
        .background(color = Color(0x88777777), shape = CircleShape)
        .width(36.dp)
        .height(8.dp)
        .align(Alignment.CenterHorizontally)
        .padding(top = 24.dp, bottom = 24.dp)
)

@Preview
@Composable
fun SheetContentPreview() {
    SheetContent()
}

@Composable
fun LayoutContent(
    onButtonClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x1f777777))
    ) {
        Text(text = "TODO: Put a nice image, and perhaps some text, Lorem ipsum")

        Button(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            Text("Get Started")
        }
    }
}