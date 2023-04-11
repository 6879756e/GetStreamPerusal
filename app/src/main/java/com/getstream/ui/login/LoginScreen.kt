@file:OptIn(ExperimentalMaterial3Api::class)

package com.getstream.ui.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.getstream.R
import com.getstream.login.LoginViewModel
import com.getstream.ui.core.CircularIndicatorWithDimmedBackground
import com.getstream.ui.theme.GetStreamPerusalTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = viewModel(),
    onSignInOptionClicked: (SignInOption) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    val isConnecting by loginViewModel.isConnecting.collectAsStateWithLifecycle()

    BottomSheetScaffold(
        sheetContent = {
            SheetContent(
                onSignInOptionClicked = onSignInOptionClicked,
                bottomSheetState = scaffoldState.bottomSheetState,
                onBackPressed = onBackPressed,
            )
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetShadowElevation = 4.dp,
    ) {
        LayoutContent(
            modifier = Modifier.closeBottomSheetOnOutsideTap(scope, scaffoldState),
            onButtonClick = {
                scope.launch { scaffoldState.bottomSheetState.expand() }
            })
    }

    if (isConnecting) {
        CircularIndicatorWithDimmedBackground()
    }
}

private fun Modifier.closeBottomSheetOnOutsideTap(
    scope: CoroutineScope,
    scaffoldState: BottomSheetScaffoldState
) = this.pointerInput(Unit) {
    detectTapGestures(onTap = {
        scope.launch {
            if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                scaffoldState.bottomSheetState.show()
            }
        }
    })
}

@Composable
fun SheetContent(
    bottomSheetState: SheetState,
    onSignInOptionClicked: (SignInOption) -> Unit = {},
    onBackPressed: () -> Unit = {},
) = Column(
    modifier = Modifier.fillMaxWidth()
) {
    val scope = rememberCoroutineScope()

    BackHandler(enabled = true) {
        if (bottomSheetState.currentValue == SheetValue.Expanded) {
            scope.launch { bottomSheetState.show() }
        } else {
            onBackPressed()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, top = 8.dp, end = 32.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SignInOption.values().forEach { signInOption ->
            SignInButton(signInOption, onSignInOptionClicked)
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "Night Mode")
@Preview(showBackground = true)
@Composable
fun SheetContentPreview() {
    GetStreamPerusalTheme {
        val bottomSheetState = rememberBottomSheetScaffoldState()
        SheetContent(bottomSheetState.bottomSheetState)
    }
}

@Composable
fun LayoutContent(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit = {},
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.sign_in_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        clipSpec = LottieClipSpec.Frame(39),
        reverseOnRepeat = true
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LottieAnimation(composition = composition, progress = { progress })

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

@Preview(showBackground = true)
@Composable
fun SignInButton(
    signInOption: SignInOption = SignInOption.GOOGLE, onClickListener: (SignInOption) -> Unit = {}
) {
    Card(
        onClick = {
            onClickListener(signInOption)
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Black, CardDefaults.shape),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 24.dp, alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = signInOption.resId),
                contentDescription = "${signInOption.type} logo",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
            Text(text = "Sign in with ${signInOption.type}")
        }
    }
}