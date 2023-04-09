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
import coil.compose.rememberAsyncImagePainter
import com.getstream.R


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
fun UserRowItem(displayName: String, imageUrl: String, isOnline: Boolean) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePhotoWithOnlineStatus(
            profileImage = { ProfileImage(imageUrl = imageUrl) },
            isOnline = isOnline,
        )
        DisplayNameText(displayName)
    }
}

@Composable
fun UserRowItem(displayName: String, @DrawableRes drawableRes: Int, isOnline: Boolean) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePhotoWithOnlineStatus(
            profileImage = { ProfileImage(drawableRes = drawableRes) },
            isOnline = isOnline,
        )
        DisplayNameText(displayName)
    }
}

@Preview
@Composable
fun ProfilePhotoWithOnlineStatusPreview() {
    ProfilePhotoWithOnlineStatus(
        isOnline = true,
        profileImage = { ProfileImage(drawableRes = R.drawable.test_user_profile_img) }
    )
}

@Composable
fun ProfilePhotoWithOnlineStatus(
    profileImage: @Composable () -> Unit,
    isOnline: Boolean = false,
) {
    Box {
        profileImage()

        OnlineIndicator(
            isOnline = isOnline,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(2.dp, 2.dp)
        )
    }
}

@Composable
private fun ProfileImage(imageUrl: String, contentDescription: String? = null) {
    Image(
        painter = rememberAsyncImagePainter(imageUrl),
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun ProfileImage(drawableRes: Int, contentDescription: String? = null) {
    Image(
        painter = painterResource(id = drawableRes),
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(4.dp)),
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun DisplayNameText(displayName: String) {
    Text(
        text = displayName,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth(1f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}