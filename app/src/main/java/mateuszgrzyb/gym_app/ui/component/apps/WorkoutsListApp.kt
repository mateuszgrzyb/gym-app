package mateuszgrzyb.gym_app.ui.component.apps

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel
import mateuszgrzyb.gym_app.ui.component.AppScaffold
import mateuszgrzyb.gym_app.ui.component.WorkoutsList
import mateuszgrzyb.gym_app.ui.component.card.WorkoutCard
import mateuszgrzyb.gym_app.ui.component.dialog.NewWorkoutDialog

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun WorkoutsListApp(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
) {
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        NewWorkoutDialog(
            onConfirm = {
                workoutsViewModel.addWorkout(it)
                openDialog = false
            },
            onDismiss = {
                openDialog = false
            }
        )
    }

    AppScaffold(
        title = stringResource(R.string.workouts_title),
        fap = {
            FloatingActionButton(
                onClick = {
                    openDialog = true
                }
            ) {
                Icon(Icons.Default.Add, null)
            }
        },

    ) { contentModifier ->
        WorkoutsList(
            modifier = contentModifier,
        ) {
            WorkoutCard(
                workout = it,
            )
        }
    }
}

