package com.getstream.util

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter

@Composable
fun AsyncImageWithPlaceholder(
    painter: AsyncImagePainter,
    placeholder: @Composable () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Crop,
    isAnimated: Boolean = true,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut()
) {
    Box(modifier = modifier) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            alignment = alignment,
            contentScale = contentScale,
            modifier = modifier
        )

        val isPlaceholderVisible by remember {
            derivedStateOf {
                painter.state is AsyncImagePainter.State.Loading || painter.state is AsyncImagePainter.State.Error
            }
        }

        if (isAnimated) {
            AnimatedVisibility(
                visible = isPlaceholderVisible,
                enter = enter,
                exit = exit,
            ) {
                placeholder()
            }
        } else {
            if (isPlaceholderVisible) placeholder()
        }
    }
}