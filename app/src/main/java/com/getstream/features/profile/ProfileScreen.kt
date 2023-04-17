package com.getstream.features.profile

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.getstream.ui.core.AsyncImageWithPlaceholder
import com.getstream.ui.core.MaxWidthOutlinedButton
import com.getstream.ui.core.OnlineIndicator
import com.getstream.util.getJobDetail
import com.getstream.util.getStatus
import io.getstream.chat.android.client.models.User


@Composable
fun ProfileScreen(
    user: User,
    isModifiable: Boolean,
    modifier: Modifier = Modifier,
    verticalContentPadding: Dp = 16.dp,
    viewModel: ProfileViewModel = hiltViewModel(),
    onEditProfileClicked: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val isEditStatusMode by viewModel.isEditStatusMode.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(verticalContentPadding))
        ProfilePhoto(user)
        UserName(user.name)
        if (user.getStatus().isNotEmpty()) Status(user.getStatus())
        if (user.getJobDetail().isNotEmpty()) Job(user.getJobDetail())
        OnlineInfo(user.online)

        if (isModifiable) {
            Status(
                isEditStatusMode,
                onStatusButtonClicked = { viewModel.toggleEditStatusMode() },
                onSubmitClicked = { viewModel.setStatus(it) }
            )

            ProfileButton(onEditProfileClicked)
        }
        Spacer(modifier = Modifier.height(verticalContentPadding))
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
private fun ProfileButton(onEditProfileClicked: () -> Unit) {
    MaxWidthOutlinedButton(onClick = onEditProfileClicked) {
        Text("Edit Profile")
    }
}


@Composable
private fun Status(
    isEditStatusMode: Boolean,
    onStatusButtonClicked: () -> Unit,
    onSubmitClicked: (String) -> Unit,
) {
    StatusButton(onClick = onStatusButtonClicked) {
        if (isEditStatusMode) {
            var textFieldValue by rememberSaveable { mutableStateOf("") }

            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    if (textFieldValue.isNotEmpty()) {
                        IconButton(onClick = { textFieldValue = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Cancel,
                                contentDescription = "Cancel input"
                            )
                        }
                    }
                },
                placeholder = { Text("Enter your status here") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    onSubmitClicked(textFieldValue)
                }),
            )
        }
    }
}

@Composable
private fun StatusButton(onClick: () -> Unit, content: @Composable () -> Unit) {
    val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

    MaxWidthOutlinedButton(onClick = onClick) {
        Column(Modifier.animateContentSize()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Face,
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 4.dp),
                    tint = color
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "What's your status?",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color = color
                )
            }
            content()
        }
    }
}

private val LEADING_ICON_SIZE = 16.dp
private val DEFAULT_SPACING = 8.dp