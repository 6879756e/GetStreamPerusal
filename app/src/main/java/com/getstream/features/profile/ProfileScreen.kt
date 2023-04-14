package com.getstream.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.getstream.ui.core.OnlineIndicator
import com.getstream.ui.core.AsyncImageWithPlaceholder
import com.getstream.util.getJobDetail
import com.getstream.util.getStatus
import io.getstream.chat.android.client.models.User


@Composable
fun ProfileScreen(
    user: User,
    isModifiable: Boolean,
    modifier: Modifier = Modifier,
    verticalContentPadding: Dp = 16.dp,
    onSetStatusClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
) {
    Surface(modifier = modifier) {
        val scrollState = rememberScrollState()

        Column(
            verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(verticalContentPadding))
            ProfilePhoto(user)
            UserName(user.name)
            if (user.getStatus().isNotEmpty()) Status(user.getStatus())
            if (user.getJobDetail().isNotEmpty()) Job(user.getJobDetail())
            OnlineInfo(user.online)

            if (isModifiable) {
                StatusButton(onSetStatusClicked)

                ProfileButton(onEditProfileClicked)
            }
            Spacer(modifier = Modifier.height(verticalContentPadding))
        }
    }
}

@Composable
private fun ProfilePhoto(user: User) {
    val profilePhotoShape = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(32.dp))

    if (user.image.isEmpty()) {
        PlaceholderProfile(profilePhotoShape, user)
    } else {
        AsyncImageWithPlaceholder(
            painter = rememberAsyncImagePainter(user.image),
            placeholder = { PlaceholderProfile(profilePhotoShape, user) },
            modifier = profilePhotoShape,
            contentDescription = null,
            isAnimated = false
        )
    }
}

@Composable
private fun PlaceholderProfile(modifier: Modifier, user: User) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        val letter = if (user.name.isNotEmpty()) user.name.first() else user.id.first()

        Text(
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayMedium,
            text = letter.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun UserName(name: String) {
    Text(text = name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
}

@Composable
private fun Status(status: String) {
    Text(text = status, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic)
}

@Composable
private fun Job(jobTitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Work,
            contentDescription = null,
            modifier = Modifier.size(LEADING_ICON_SIZE)
        )
        Spacer(modifier = Modifier.width(DEFAULT_SPACING))

        Text(text = jobTitle, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun OnlineInfo(isOnline: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DEFAULT_SPACING)
    ) {
        OnlineIndicator(isOnline = isOnline, size = LEADING_ICON_SIZE)
        Text(if (isOnline) "Online" else "Away", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun StatusButton(onSetStatusClicked: () -> Unit) {
    OutlinedButton(
        onClick = { onSetStatusClicked() },
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text("Set a status")
    }
}

@Composable
private fun ProfileButton(onEditProfileClicked: () -> Unit) {
    OutlinedButton(
        onClick = { onEditProfileClicked() },
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth(1f)
    ) {
        Text("Edit Profile")
    }
}

private val LEADING_ICON_SIZE = 16.dp
private val DEFAULT_SPACING = 8.dp