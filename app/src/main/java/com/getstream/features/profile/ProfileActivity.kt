package com.getstream.features.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

        require(intent.getStringExtra(USER_ID_KEY) != null)
        setContent {
            GetStreamPerusalTheme {
                val user by viewModel.user.collectAsStateWithLifecycle()

                user?.run {
                    ProfileScreen(
                        user = this,
                        isModifiable = isSelf(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        onSetStatusClicked = {},
                        onEditProfileClicked = {}
                    )
                } ?: CircularIndicatorWithDimmedBackground()
            }
        }

        onErrorLoading()
    }

    private fun onErrorLoading() {
        viewModel.errorLoading.observe(this) { isError ->
            if (isError) finish()
        }
    }
}