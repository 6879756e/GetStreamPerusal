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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getstream.ui.theme.GetStreamPerusalTheme
import kotlinx.coroutines.launch


@Composable
fun LoginScreen() {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = { SheetContent() },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    ) {
        LayoutContent(onButtonClick = {
            scope.launch { scaffoldState.bottomSheetState.expand() }
        })
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
fun SheetContent() = Column(
    modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.8f)
) {
    Spacer(modifier = Modifier.padding(vertical = 8.dp))
    BottomSheetHandle()

    Text("TODO: BottomSheet Content")
}

@Preview
@Composable
private fun ColumnScope.BottomSheetHandle() = Box(
    modifier = Modifier
        .background(color = Color(0x88777777), shape = CircleShape)
        .width(24.dp)
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