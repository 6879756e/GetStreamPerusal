package com.getstream.features.profile

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.getstream.ui.core.AsyncImageWithPlaceholder
import com.getstream.ui.core.MaxWidthOutlinedButton
import com.getstream.ui.core.OnlineIndicator
import com.getstream.util.clickable
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
    onProfileImageChangeRequested: (ProfileImageRequest) -> Unit,
) {
    val scrollState = rememberScrollState()
    val isEditMode by viewModel.isEditMode.collectAsStateWithLifecycle()

    val username by viewModel.username.collectAsStateWithLifecycle()
    val status by viewModel.status.collectAsStateWithLifecycle()
    val job by viewModel.job.collectAsStateWithLifecycle()

    Column(
        verticalArrangement = Arrangement.spacedBy(DEFAULT_SPACING),
        modifier = modifier.verticalScroll(scrollState)
    ) {
        Spacer(modifier = Modifier.height(verticalContentPadding))
        ProfilePhoto(user, isEditMode) {
            viewModel.setProfileImageDialogVisible()
        }
        UserName(
            textFieldValue = username,
            onValueChange = { viewModel.setUserName(it) },
            isEditMode = isEditMode,
        )
        if (user.getStatus().isNotEmpty() || isEditMode) {
            Status(
                textFieldValue = status, onValueChange = { viewModel.setUserStatus(it) }, isEditMode
            )
        }
        if (user.getJobDetail().isNotEmpty() || isEditMode) {
            Job(
                textFieldValue = job, onValueChange = { viewModel.setJobDetail(it) }, isEditMode
            )
        }
        OnlineInfo(user.online)

        if (isModifiable) {
            EditProfile(isEditMode, viewModel)
        }
        Spacer(modifier = Modifier.height(verticalContentPadding))
    }

    val profileImageDialogVisible by viewModel.profileImageDialogVisible.collectAsStateWithLifecycle()

    if (profileImageDialogVisible) {
        Dialog(onDismissRequest = { viewModel.setProfileImageDialogGone() }) {
            Card {
                Column(modifier = Modifier.padding(32.dp)) {
                    Text(
                        text = "Update your profile image",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    ImageOptions(onProfileImageChangeRequested)
                }
            }
        }
    }
}

@Composable
private fun ImageOptions(onProfileImageChangeRequested: (ProfileImageRequest) -> Unit) {
    ProfileImageRequest.values().forEachIndexed { index, profileImageRequest ->
        ImageOptionItem(onProfileImageChangeRequested, profileImageRequest)

        if (index != ProfileImageRequest.values().lastIndex) {
            Divider(modifier = Modifier.padding(4.dp))
        }
    }
}

@Composable
private fun ImageOptionItem(
    onProfileImageChangeRequested: (ProfileImageRequest) -> Unit,
    request: ProfileImageRequest
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable { onProfileImageChangeRequested(request) }
            .padding(8.dp),
    ) {
        Icon(imageVector = request.imageVector, contentDescription = "")
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = request.text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun FocusManager.moveFocusDown() {
    moveFocus(FocusDirection.Down)
}

@Composable
private fun ProfilePhoto(
    user: User,
    isEditMode: Boolean,
    onImageClicked: () -> Unit
) {
    val profilePhotoShape = Modifier
        .fillMaxWidth()
        .aspectRatio(1f)
        .clip(RoundedCornerShape(32.dp))

    Box {
        if (user.image.isEmpty()) {
            PlaceholderProfile(profilePhotoShape, user)
        } else {
            val context = LocalContext.current

            AsyncImageWithPlaceholder(
                painter = rememberAsyncImagePainter(
                    remember(user.image) {
                        ImageRequest.Builder(context)
                            .data(user.image)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .memoryCachePolicy(CachePolicy.DISABLED)
                            .build()
                    }
                ),
                placeholder = { PlaceholderProfile(profilePhotoShape, user) },
                modifier = profilePhotoShape,
                contentDescription = null,
                isAnimated = false
            )
        }

        AnimatedVisibility(
            visible = isEditMode,
            enter = slideIn { IntOffset(0, it.height) } + fadeIn(),
            exit = slideOut { IntOffset(0, it.height) } + fadeOut(),
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            IconButton(
                onClick = onImageClicked,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    imageVector = if (user.image.isEmpty()) Icons.Outlined.AddAPhoto else Icons.Outlined.Image,
                    contentDescription = "Click to change profile",
                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onPrimary),
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderProfile(modifier: Modifier, user: User) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    ) {
        val letter = if (user.name.isNotEmpty()) user.name.first() else user.id.firstOrNull()

        Text(
            modifier = Modifier.align(Alignment.Center),
            style = MaterialTheme.typography.displayMedium,
            text = letter.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun UserName(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isEditMode: Boolean,
) {
    val focusRequester = FocusRequester()

    if (isEditMode) {
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        enabled = isEditMode,
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.moveFocusDown()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        modifier = Modifier.focusRequester(focusRequester)
    )
}

@Composable
private fun Status(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isEditMode: Boolean
) {
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = textFieldValue,
        onValueChange = onValueChange,
        enabled = isEditMode,
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontStyle = FontStyle.Italic,
            color = LocalContentColor.current
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.moveFocusDown()
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

@Composable
private fun Job(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isEditMode: Boolean
) {
    val focusManager = LocalFocusManager.current

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.Work,
            contentDescription = null,
            modifier = Modifier.size(LEADING_ICON_SIZE)
        )
        Spacer(modifier = Modifier.width(DEFAULT_SPACING))

        BasicTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            enabled = isEditMode,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = LocalContentColor.current),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
        )
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
private fun EditProfile(
    isEditMode: Boolean,
    viewModel: ProfileViewModel
) {
    Row(modifier = Modifier.animateContentSize()) {
        if (isEditMode) {
            OutlinedButton(onClick = {
                viewModel.cancelEdit()
            }, modifier = Modifier.weight(1f)) {
                Text("Cancel", color = MaterialTheme.colorScheme.secondary)
            }
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedButton(
                onClick = { viewModel.updateUser() }, modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        } else {
            ProfileButton {
                viewModel.toggleEditMode()
            }
        }
    }
}

private val LEADING_ICON_SIZE = 16.dp
private val DEFAULT_SPACING = 8.dp