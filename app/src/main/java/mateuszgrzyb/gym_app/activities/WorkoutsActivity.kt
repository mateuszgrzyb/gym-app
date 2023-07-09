package mateuszgrzyb.gym_app.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import mateuszgrzyb.gym_app.ui.theme.GymappTheme
import dagger.hilt.android.AndroidEntryPoint
import mateuszgrzyb.gym_app.ui.components.WorkoutDetailsApp
import mateuszgrzyb.gym_app.ui.components.WorkoutsListApp
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel


@ExperimentalMaterial3Api
@AndroidEntryPoint
class WorkoutsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            GymappTheme(darkTheme = false) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val workoutsViewModel: WorkoutsViewModel by viewModels()
                    val currentWorkout = workoutsViewModel.currentWorkout;

                    if (currentWorkout == null) {
                        WorkoutsListApp()
                    } else {
                        WorkoutDetailsApp(
                            workout = currentWorkout,
                        )
                    }
                }
            }
        }
    }
}


