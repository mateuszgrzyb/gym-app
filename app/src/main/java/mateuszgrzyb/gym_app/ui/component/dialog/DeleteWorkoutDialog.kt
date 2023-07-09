package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Workout
import mateuszgrzyb.gym_app.displayName

@Composable
fun DeleteWorkoutDialog(
    workout: Workout,
    onConfirm: (Workout) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                ),
                onClick = { onConfirm(workout) }
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = {
            Text(stringResource(R.string.delete_workout_title))
        },
        text = {
            Text(stringResource(R.string.delete_workout_body, displayName(workout)))
        }
    )
}