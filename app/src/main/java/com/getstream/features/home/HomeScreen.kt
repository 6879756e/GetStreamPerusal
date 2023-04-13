package com.getstream.features.home

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getstream.ui.core.CircularIndicatorWithDimmedBackground
import com.getstream.ui.core.UserRowItem
import com.getstream.util.clickable
import com.getstream.util.combinedClickable
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onChannelCreated: (Channel) -> Unit,
    onUserClicked: (User) -> Unit,
) {
    val isChannelCreateMode by viewModel.channelCreateMode.collectAsStateWithLifecycle()

    val users = viewModel.users
    val selectedUsers = viewModel.selectedUsers

    val topBarState by remember {
        derivedStateOf { if (!isChannelCreateMode) HomeViewModel.TopBarState.START_CHAT else HomeViewModel.TopBarState.CANCEL_CHAT }
    }

    val buttonVisibility by remember { derivedStateOf { isChannelCreateMode && selectedUsers.isNotEmpty() } }

    BackHandler(isChannelCreateMode) {
        viewModel.toggleChannelCreateMode()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBar(topBarState) {
                viewModel.toggleChannelCreateMode()
            }

            UsersList(users = users,
                isUserSelectState = isChannelCreateMode,
                selectedUsers = selectedUsers.keys,
                onClick = { if (!isChannelCreateMode) { onUserClicked(it) } },
                onLongClick = { if (!isChannelCreateMode) viewModel.startChannelCreateMode() }) { user, isSelected ->
                viewModel.setUserSelected(user, isSelected)
            }

        }

        Animate(buttonVisibility) {
            CreateChannelButton(onClick = {
                viewModel.createChannel(onSuccess = { onChannelCreated(it) })
            })
        }

        val isChannelBeingCreated by viewModel.isChannelBeingCreated.collectAsStateWithLifecycle()
        if (isChannelBeingCreated) {
            CircularIndicatorWithDimmedBackground()
        }
    }
}

@Composable
private fun TopBar(
    topBarState: HomeViewModel.TopBarState,
    onIconClicked: (HomeViewModel.TopBarState) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Colleagues",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onIconClicked(topBarState) }) {
            Icon(
                imageVector = topBarState.imageVector,
                contentDescription = "",
                modifier = Modifier
                    .size(48.dp)
                    .padding(8.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun UsersList(
    users: List<User>,
    isUserSelectState: Boolean,
    selectedUsers: Set<String>,
    modifier: Modifier = Modifier,
    onClick: (User) -> Unit,
    onLongClick: (User) -> Unit,
    onCheckedChange: (User, Boolean) -> Unit,
) {
    LazyColumn {
        items(
            users,
            key = { it.id },
        ) { user ->
            val isChecked = user.id in selectedUsers
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .getClickPolicy(
                        isUserSelectState = isUserSelectState,
                        user = user,
                        isChecked = isChecked,
                        onCheckedChange = onCheckedChange,
                        onClick = onClick,
                        onLongClick = onLongClick
                    )
                    .animateItemPlacement(tween(300))
            ) {
                AnimatedVisibility(visible = isUserSelectState) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onCheckedChange(user, !isChecked) },
                        modifier = Modifier
                            .padding(12.dp)
                            .size(24.dp)
                    )
                }

                val horizontalPadding = if (isUserSelectState) 0.dp else 8.dp
                UserRowItem(
                    displayName = user.name,
                    imageUrl = user.image,
                    isOnline = user.online,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = horizontalPadding)
                )
            }
        }
    }
}

private fun Modifier.getClickPolicy(
    isUserSelectState: Boolean,
    user: User,
    isChecked: Boolean,
    onCheckedChange: (User, Boolean) -> Unit,
    onClick: (User) -> Unit,
    onLongClick: (User) -> Unit
) = this then if (isUserSelectState) {
    background(if (isChecked) Color.LightGray else Color.Transparent)
    clickable {
        onCheckedChange(user, !isChecked)
    }
} else {
    combinedClickable(
        onClick = { onClick(user) },
        onLongClick = {
            onLongClick(user)
            onCheckedChange(user, !isChecked)
        },
    )
}

@Composable
private fun BoxScope.Animate(buttonVisibility: Boolean, button: @Composable () -> Unit) {
    AnimatedVisibility(
        visible = buttonVisibility,
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .wrapContentSize()
            .padding(horizontal = 40.dp, vertical = 8.dp)
            .offset(x = 0.dp, y = (-16).dp),
        enter = fadeIn() + slideIn(initialOffset = { IntOffset(0, it.height) }),
        exit = fadeOut() + slideOut(targetOffset = { IntOffset(0, it.height) })
    ) { button() }
}

@Composable
fun CreateChannelButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier, onClick = onClick
    ) {
        Text("Create Channel", modifier = Modifier.align(Alignment.CenterVertically))
    }
}