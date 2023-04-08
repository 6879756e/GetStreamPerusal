package com.getstream.ui.core

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun OnlineIndicator(isOnline: Boolean, size: Dp = 12.dp) {
    if (isOnline) {
        // TODO: Find an alternative (more elegant) method
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.background, CircleShape)
        ) {
            Box(
                modifier = Modifier
                    .size(size - 2.dp)
                    .background(color = Color(0xFF003200), shape = CircleShape)
                    .align(Alignment.Center)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(size = size)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Box(
                modifier = Modifier
                    .size(size - 4.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.LightGray, shape = CircleShape)
                    .align(Alignment.Center)
            )
        }
    }
}