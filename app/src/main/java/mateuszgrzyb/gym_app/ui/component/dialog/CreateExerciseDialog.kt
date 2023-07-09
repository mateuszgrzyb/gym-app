package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Exercise

@ExperimentalMaterial3Api
@Composable
fun CreateExerciseDialog(
    workoutId: Long,
    onConfirm: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    BaseExerciseDialog(
        confirmText = stringResource(R.string.create),
        title = stringResource(R.string.create_exercise_title),
        exercise = null,
        workoutId = workoutId,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}