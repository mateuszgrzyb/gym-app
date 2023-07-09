package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Workout

@ExperimentalMaterial3Api
@Composable
fun NewWorkoutDialog(
    onConfirm: (Workout) -> Unit,
    onDismiss: () -> Unit,
) {
    BaseWorkoutDialog(
        confirmText = stringResource(R.string.create),
        title = stringResource(R.string.create_workout_title),
        workout = null,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}