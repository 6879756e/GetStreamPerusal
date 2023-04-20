package com.getstream.features.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.getstream.features.USER_ID_KEY
import com.getstream.ui.core.CircularIndicatorWithDimmedBackground
import com.getstream.ui.theme.GetStreamPerusalTheme
import com.getstream.util.isSelf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : ComponentActivity() {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val chooseFromGallery = activityResultLauncherImageFromGallery()
        val takePicture = activityResultLauncherTakePictureFromCamera()

        require(intent.getStringExtra(USER_ID_KEY) != null)
        setContent {
            GetStreamPerusalTheme {
                val user by viewModel.user.collectAsStateWithLifecycle()

                if (user.id.isNotEmpty()) {
                    Surface {
                        ProfileScreen(
                            user = user,
                            isModifiable = user.isSelf(),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) { option ->
                            when (option) {
                                ProfileImageRequest.CAMERA -> takePicture.launch(viewModel.getUri())
                                ProfileImageRequest.GALLERY -> {
                                    chooseFromGallery.launch(
                                        PickVisualMediaRequest(
                                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                }
                                ProfileImageRequest.CLEAR -> {
                                    viewModel.clearUserImage()
                                }
                            }
                        }
                    }
                } else {
                    CircularIndicatorWithDimmedBackground()
                }
            }

            onErrorLoading()
        }
    }

    private fun activityResultLauncherTakePictureFromCamera() =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                val uri = viewModel.getUri()
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)

                val rotatedBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    Matrix().apply { postRotate(90f) },
                    true
                )
                viewModel.uploadImage(rotatedBitmap)
            }
        }

    private fun activityResultLauncherImageFromGallery() =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(contentResolver, uri)

                    val bitmap = ImageDecoder.decodeBitmap(source)

                    viewModel.uploadImage(bitmap)
                } else {
                    val inputStream = contentResolver.openInputStream(uri)

                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    viewModel.uploadImage(bitmap)
                }
            }
        }

    private fun onErrorLoading() {
        viewModel.errorLoading.observe(this) { isError ->
            if (isError) finish()
        }
    }
}