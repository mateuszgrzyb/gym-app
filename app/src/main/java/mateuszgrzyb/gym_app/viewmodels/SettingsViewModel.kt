package mateuszgrzyb.gym_app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.db.DB
import mateuszgrzyb.gym_app.db.Settings
import javax.inject.Inject

enum class PermissionsState {
    Unverified,
    Ok,
    Failed,
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val db: DB
): ViewModel() {

    val optionalSettings = db.settingsDao().getOptional().asLiveData()
    val settings = db.settingsDao().get().asLiveData()

    var permissionsState = MutableLiveData(PermissionsState.Unverified)

    init {
        viewModelScope.launch {
            val s = db.settingsDao().getCoro()

            if (s == null) {
                db.settingsDao().insert(Settings())
            }

        }
    }

    fun updateSettings(newSettings: Settings) {
        viewModelScope.launch {
            if (newSettings.id == 0L) {
                val id = db.settingsDao().insert(newSettings)
                newSettings.id = id
            } else {
                db.settingsDao().update(newSettings)
            }
        }
    }
}