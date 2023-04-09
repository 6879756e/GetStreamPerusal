package com.getstream.ui.core

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.getstream.ui.theme.GetStreamPerusalTheme


@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Preview(showBackground = true)
@Composable
fun OnlineIndicatorOfflinePreview() {
    OnlineIndicator(false)
}

@Preview(uiMode = UI_MODE_NIGHT_YES, showBackground = true, name = "Dark mode")
@Preview(showBackground = true)
@Composable
fun OnlineIndicatorOnlinePreview() {
    GetStreamPerusalTheme {
        OnlineIndicator(true)
    }
}

@Composable
fun OnlineIndicator(isOnline: Boolean, modifier: Modifier = Modifier, size: Dp = 10.dp) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.background)
            .padding(2.dp)
            .clip(CircleShape)
    ) {
        val innerCircleModifier = if (isOnline) {
            Modifier.background(Color(0xFF003200))
        } else {
            Modifier.border(1.dp, Color.LightGray, CircleShape)
        }

        Box(modifier = innerCircleModifier.fillMaxSize())
    }
}