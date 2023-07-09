package mateuszgrzyb.gym_app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel
import mateuszgrzyb.gym_app.displayName

@ExperimentalMaterial3Api
@Composable
fun WorkoutsListApp(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        NewWorkoutDialog(
            onConfirm = {
                coroutineScope.launch {
                    val workoutId = workoutsViewModel.add(it)
                    openDialog = false
                    workoutsViewModel.setCurrentWorkout(workoutId)
                }
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
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                )
            }
        },

    ) { contentModifier ->
        WorkoutsList(modifier = contentModifier)
    }
}

@ExperimentalMaterial3Api
@Composable
fun WorkoutDetailsApp(
    workout: WorkoutWithExercises,
    workoutsViewModel: WorkoutsViewModel = viewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    var editing by remember { mutableStateOf(false) }
    val title = displayName(workout.workout)

    var openNewExerciseDialog by remember { mutableStateOf(false) }
    var openDeleteWorkoutDialog by remember { mutableStateOf(false) }

    if (openNewExerciseDialog) {
        NewExerciseDialog(
            workoutId = workout.workout.id,
            onConfirm = {
                coroutineScope.launch {
                    workoutsViewModel.addExercise(it)
                }
                openNewExerciseDialog = false
            },
            onDismiss = {
                openNewExerciseDialog = false
            },
        )
    }

    if (openDeleteWorkoutDialog) {
        DeleteWorkoutDialog(
            workout = workout.workout,
            onConfirm = {
                coroutineScope.launch {
                    workoutsViewModel.deleteWorkout(workout.workout)
                    workoutsViewModel.setCurrentWorkout(null)
                }
                openDeleteWorkoutDialog = false
            },
            onDismiss = {
                openDeleteWorkoutDialog = false
            }
        )
    }

    AppScaffold(
        title = if (editing) { stringResource(R.string.editing_title, title) } else { title },
        actions = {
            IconButton(onClick = { editing = !editing }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { openDeleteWorkoutDialog = true }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                )
            }
            IconButton(onClick = { workoutsViewModel.setCurrentWorkout(null) }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
            }
        },
        fap = {
            if (editing) {
                FloatingActionButton(
                    onClick = {
                        openNewExerciseDialog = true
                    }
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                    )
                }
            }
        },
    ) { contentModifier ->
        WorkoutDetail(
            workout = workout,
            modifier = contentModifier,
        ) {
            ExerciseCard(
                editable = editing,
                exercise = it,
            )
        }
    }
}
