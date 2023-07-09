package mateuszgrzyb.gym_app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.db.DB
import mateuszgrzyb.gym_app.db.Settings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val db: DB
): ViewModel() {
    var settings by mutableStateOf(Settings())
        private set

    init {
        viewModelScope.launch {
            db.settingsDao().get().collectLatest { s ->
                settings = s
            }
        }
    }

    suspend fun updateSettings(newSettings: Settings) {
        db.settingsDao().upsert(newSettings)
        settings = newSettings
    }
}