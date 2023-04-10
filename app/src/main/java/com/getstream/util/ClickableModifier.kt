package com.getstream.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role


fun Modifier.clickable(
    indication: Indication? = null,
    onClick: () -> Unit
) = this.clickable(
    interactionSource = MutableInteractionSource(),
    indication = indication,
    enabled = true,
    onClickLabel = null,
    role = null,
    onClick,
)


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.combinedClickable(
    indication: Indication? = null,
    onClickLabel: String? = null,
    role: Role? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    onClick: () -> Unit
) = this.combinedClickable(
    interactionSource = MutableInteractionSource(),
    indication = indication,
    enabled = true,
    onClickLabel = onClickLabel,
    role = role,
    onLongClickLabel = onLongClickLabel,
    onLongClick = onLongClick,
    onDoubleClick = onDoubleClick,
    onClick = onClick
)