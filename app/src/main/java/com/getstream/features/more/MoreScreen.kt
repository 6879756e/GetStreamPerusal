package com.getstream.features.more

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.getstream.ui.core.MaxWidthOutlinedButton
import com.getstream.ui.core.TopBar
import com.getstream.ui.core.UserRowItem
import com.getstream.util.clickable
import io.getstream.chat.android.client.models.User


@Composable
fun MoreScreen(
    viewModel: MoreViewModel = viewModel(),
    onUserClicked: (User) -> Unit,
) {
    val user by viewModel.user.collectAsStateWithLifecycle()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            TopBar(title = "Settings")

            ProfileScreenColumn {
                User(user, onUserClicked)
                StatusButton { }
                OnlineStatus(user.online)
            }
            Divider()
            ProfileScreenColumn {
                EditProfile()
                Preferences()
            }
        }

    }
}

@Composable
private fun ProfileScreenColumn(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier.padding(12.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    content()
}

@Composable
private fun User(
    user: User,
    onUserClicked: (User) -> Unit
) {
    UserRowItem(
        displayName = user.name,
        imageUrl = user.image,
        isOnline = user.online,
        modifier = Modifier.clickable {
            onUserClicked(user)
        }
    )
}

@Composable
private fun StatusButton(onClick: () -> Unit) {
    val color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)

    MaxWidthOutlinedButton(onClick = onClick) {
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
}

@Composable
private fun ProfileScreenRow(modifier: Modifier = Modifier, content: @Composable () -> Unit) =
    Row(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content()
    }

@Composable
private fun OnlineStatus(isOnline: Boolean, modifier: Modifier = Modifier) {
    val preStatusText = "Set yourself as "
    val text = "Set yourself as ${if (isOnline) "away" else "active"}"
    val annotatedText = AnnotatedString(
        text = text, spanStyles = listOf(
            AnnotatedString.Range(
                item = SpanStyle(
                    fontWeight = FontWeight.Bold,
                ), start = preStatusText.length, end = text.length
            )
        )
    )

    ProfileScreenRow(modifier) {
        Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
        Text(annotatedText)
    }
}

@Composable
private fun EditProfile(modifier: Modifier = Modifier) {
    ProfileScreenRow(modifier) {
        Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
        Text("Edit profile")
    }
}

@Composable
fun Preferences(modifier: Modifier = Modifier) {
    ProfileScreenRow(modifier) {
        Icon(imageVector = Icons.Outlined.SettingsSuggest, contentDescription = null)
        Text("Preferences")
    }
}