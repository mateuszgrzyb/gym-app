package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Exercise

@ExperimentalMaterial3Api
@Composable
fun UpdateExerciseDialog(
    workoutId: Long,
    exercise: Exercise,
    onConfirm: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    BaseExerciseDialog(
        confirmText = stringResource(R.string.update),
        title = stringResource(R.string.update_exercise_title),
        exercise = exercise,
        workoutId = workoutId,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}