package mateuszgrzyb.gym_app.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mateuszgrzyb.gym_app.ui.component.apps.SettingsApp
import mateuszgrzyb.gym_app.ui.theme.GymappTheme
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class SettingsActivity : BaseActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GymappTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val settings by settingsViewModel.optionalSettings.observeAsState()

                    if (settings != null) {
                        SettingsApp(
                            settings = settings!!,
                            updateSettings = { settingsViewModel.updateSettings(it) }
                        )
                    }
                }
            }
        }
    }
}

