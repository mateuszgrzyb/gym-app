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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.ui.component.apps.WorkoutDetailsApp
import mateuszgrzyb.gym_app.ui.component.apps.WorkoutsListApp
import mateuszgrzyb.gym_app.ui.theme.GymappTheme
import mateuszgrzyb.gym_app.viewmodels.PermissionsState
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel


@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@AndroidEntryPoint
class WorkoutsActivity : BaseActivity() {
    private val workoutsViewModel: WorkoutsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            GymappTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val currentWorkout by workoutsViewModel.currentWorkout.observeAsState()

                    when (currentWorkout) {
                        null -> WorkoutsListApp()
                        else -> WorkoutDetailsApp(workout = currentWorkout!!)
                    }
                }
            }
        }
    }
}


