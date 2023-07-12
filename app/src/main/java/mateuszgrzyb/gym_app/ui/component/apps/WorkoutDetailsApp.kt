package mateuszgrzyb.gym_app.ui.component.apps

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import mateuszgrzyb.gym_app.displayName
import mateuszgrzyb.gym_app.ui.component.AppScaffold
import mateuszgrzyb.gym_app.ui.component.ReorderWorkoutDetail
import mateuszgrzyb.gym_app.ui.component.WorkoutDetail
import mateuszgrzyb.gym_app.ui.component.card.ExerciseCard
import mateuszgrzyb.gym_app.ui.component.dialog.CreateExerciseDialog
import mateuszgrzyb.gym_app.ui.component.dialog.DeleteWorkoutDialog
import mateuszgrzyb.gym_app.ui.component.dialog.WorkoutCalendarDialog
import mateuszgrzyb.gym_app.viewmodels.PermissionsState
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel
import mateuszgrzyb.gym_app.viewmodels.SnackbarViewModel
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel

enum class DialogState {
    Closed,
    OpenNewExercise,
    OpenDeleteWorkout,
    OpenAddToCalendar,
}

enum class DetailsState {
    Display,
    Edit,
    Reorder,
}

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun WorkoutDetailsApp(
    snackbarViewModel: SnackbarViewModel = viewModel(),
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    settingsViewModel: SettingsViewModel = viewModel(),
    workout: WorkoutWithExercises,
) {
    val permissionsState by settingsViewModel.permissionsState.observeAsState(PermissionsState.Unverified)

    val title = displayName(workout.workout)

    var detailsState by remember { mutableStateOf(DetailsState.Display) }
    var dialogState by remember { mutableStateOf(DialogState.Closed) }

    fun resetDialogState() {
        dialogState = DialogState.Closed
    }

    when (dialogState) {
        DialogState.Closed -> {}
        DialogState.OpenNewExercise -> {
            CreateExerciseDialog(
                workoutId = workout.workout.id,
                onConfirm = {
                    workoutsViewModel.addExercise(it)
                    resetDialogState()
                },
                onDismiss = ::resetDialogState,
            )
        }
        DialogState.OpenDeleteWorkout -> {
            DeleteWorkoutDialog(
                workout = workout.workout,
                onConfirm = {
                    workoutsViewModel.deleteWorkout(workout.workout)
                    resetDialogState()
                },
                onDismiss = ::resetDialogState
            )
        }
        DialogState.OpenAddToCalendar -> {
            if (permissionsState == PermissionsState.Ok) {
                WorkoutCalendarDialog(
                    workout = workout.workout,
                    onConfirm = ::resetDialogState,
                    onDismiss = ::resetDialogState,
                )
            }
        }
    }

    LaunchedEffect(dialogState, permissionsState) {
        if (dialogState == DialogState.OpenAddToCalendar) {
            when (permissionsState) {
                PermissionsState.Ok -> {}
                PermissionsState.Unverified -> {
                    snackbarViewModel.hostState.showSnackbar("INVALID STATE")
                    resetDialogState()
                }
                PermissionsState.Failed -> {
                    snackbarViewModel.hostState.showSnackbar("Failed")
                    resetDialogState()
                }
            }
        }
    }

    AppScaffold(
        title = when (detailsState) {
            DetailsState.Edit -> stringResource(R.string.editing_title, title)
            DetailsState.Display -> title
            DetailsState.Reorder -> stringResource(R.string.reordering_title, title)
        },
        actions = {
            IconButton(
                onClick = {
                    dialogState = DialogState.OpenAddToCalendar
                }
            ) {
                Icon(Icons.Filled.DateRange, null)
            }
            IconButton(
                onClick = {
                    detailsState = when (detailsState) {
                        DetailsState.Display -> DetailsState.Edit
                        DetailsState.Edit -> DetailsState.Display
                        DetailsState.Reorder -> DetailsState.Edit
                    }
                }
            ) {
                Icon(Icons.Filled.Edit, null)
            }
            IconButton(
                onClick = {
                    detailsState = when (detailsState) {
                        DetailsState.Display -> DetailsState.Reorder
                        DetailsState.Edit -> DetailsState.Reorder
                        DetailsState.Reorder -> DetailsState.Display
                    }
                }
            ) {
                Icon(Icons.Filled.MoreVert, null)
            }
            if (detailsState == DetailsState.Edit) {
                IconButton(
                    onClick = {
                        dialogState = DialogState.OpenDeleteWorkout
                    }
                ) {
                    Icon(Icons.Filled.Delete, null)
                }
            }
            IconButton(
                onClick = {
                    when (detailsState) {
                        DetailsState.Display -> workoutsViewModel.setCurrentWorkout(null)
                        DetailsState.Edit -> detailsState = DetailsState.Display
                        DetailsState.Reorder -> detailsState = DetailsState.Display
                    }
                }
            ) {
                Icon(Icons.Filled.ArrowBack, null)
            }
        },
        fap = {
            if (detailsState == DetailsState.Edit) {
                FloatingActionButton(
                    onClick = {
                        dialogState = DialogState.OpenNewExercise
                    }
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
        },
    ) { contentModifier ->
        when (detailsState) {
            DetailsState.Display, DetailsState.Edit ->
                WorkoutDetail(
                    //workout = workout,
                    modifier = contentModifier,
                ) { exercise ->
                    ExerciseCard(
                        workoutId = workout.workout.id,
                        detailsState = detailsState,
                        exercise = exercise,
                    )
                }
            DetailsState.Reorder ->
                ReorderWorkoutDetail(
                    //workout = workout,
                    modifier = contentModifier,
                ) { exercise, isDragged ->
                    ExerciseCard(
                        workoutId = workout.workout.id,
                        detailsState = detailsState,
                        exercise = exercise,
                        isDragged = isDragged,
                    )
                }
        }
    }
}