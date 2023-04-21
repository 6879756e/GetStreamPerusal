package com.getstream.features.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HideImage
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.ui.graphics.vector.ImageVector

enum class ProfileImageRequest(
    val imageVector: ImageVector,
    val text: String,
) {
    GALLERY(
        Icons.Default.PhotoLibrary,
        "Choose from gallery"
    ),
    CAMERA(
        Icons.Default.PhotoCamera,
        "Take a picture"
    ),
    CLEAR(
        Icons.Default.HideImage,
        "Clear profile image"
    )
}