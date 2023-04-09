package com.getstream.ui.core

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.getstream.R

@Composable
fun UserRowItem(displayName: String, @DrawableRes drawableRes: Int, isOnline: Boolean) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePhotoWithOnlineStatus(
            isOnline = isOnline,
            drawableRes = drawableRes
        )
        Text(
            text = displayName,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun UserRowItemPreview(isOnline: Boolean = true) {
    UserRowItem("Philip J. Fry ", R.drawable.test_user_profile_img, isOnline)
}

@Preview
@Composable
fun UserRowItemPreviewLongName(isOnline: Boolean = true) {
    val name = StringBuilder()
    repeat(10) { name.append("Philip J. Fry ") }
    UserRowItem(name.toString(), R.drawable.test_user_profile_img, isOnline)
}


@Composable
fun ProfilePhotoWithOnlineStatus(
    @DrawableRes drawableRes: Int,
    isOnline: Boolean = false,
    contentDescription: String? = null,
) {
    Box {
        Image(
            painter = painterResource(id = drawableRes),
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        )

        OnlineIndicator(
            isOnline = isOnline,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(2.dp, 2.dp)
        )
    }
}

@Preview
@Composable
fun ProfilePhotoWithOnlineStatusPreview() {
    ProfilePhotoWithOnlineStatus(isOnline = true, drawableRes = R.drawable.test_user_profile_img)
}