package mateuszgrzyb.gym_app.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel

class SnackbarViewModel : ViewModel() {
    val hostState = SnackbarHostState()
}