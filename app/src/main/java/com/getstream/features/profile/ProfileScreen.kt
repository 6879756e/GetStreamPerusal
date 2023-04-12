package com.getstream.features.profile

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.getstream.ui.core.OnlineIndicator
import io.getstream.chat.android.client.models.User


@Composable
fun ProfileScreen(
    user: User,
    isModifiable: Boolean,
    modifier: Modifier = Modifier,
    onSetStatusClicked: () -> Unit,
    onEditProfileClicked: () -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(12.dp)
    ) {
        val scrollState = rememberScrollState()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Image(
                painter = rememberAsyncImagePainter(user.image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(32.dp)),
                contentScale = ContentScale.Crop
            )

            Text(
                text = user.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (user.extraData.containsKey("status")) {
                Text(
                    text = user.extraData["status"].toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic
                )
            }

            if (user.extraData.containsKey("job")) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Work,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = user.extraData["job"].toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OnlineIndicator(isOnline = user.online, size = 16.dp)
                Text(
                    if (user.online) "Online" else "Away",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (isModifiable) {
                OutlinedButton(
                    onClick = { onSetStatusClicked() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    Text("Set a status")
                }

                OutlinedButton(
                    onClick = { onEditProfileClicked() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(1f)
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}