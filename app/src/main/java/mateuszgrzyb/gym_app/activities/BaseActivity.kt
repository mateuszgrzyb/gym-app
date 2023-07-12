package mateuszgrzyb.gym_app.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import mateuszgrzyb.gym_app.viewmodels.PermissionsState
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel

@ExperimentalMaterial3Api
abstract class BaseActivity : ComponentActivity() {
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        settingsViewModel.permissionsState.value = when (it) {
            true -> PermissionsState.Ok
            false -> PermissionsState.Failed
        }
    }

    private fun requestWorkoutPermissions() {
        when {
            checkSelfPermission(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED -> {
                settingsViewModel.permissionsState.value = PermissionsState.Ok
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_CALENDAR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsViewModel.permissionsState.observe(this) {
            when (it) {
                PermissionsState.Unverified -> requestWorkoutPermissions()
                else -> {}
            }
        }
    }
}