package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Workout
import mateuszgrzyb.gym_app.db.WorkoutWithExercises

@ExperimentalMaterial3Api
@Composable
fun UpdateWorkoutDialog(
    workout: WorkoutWithExercises,
    onConfirm: (Workout) -> Unit,
    onDismiss: () -> Unit,
) {
    BaseWorkoutDialog(
        confirmText = stringResource(R.string.update),
        title = stringResource(R.string.update_workout_title),
        workout = workout,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}