package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import mateuszgrzyb.gym_app.db.Workout
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.cap
import mateuszgrzyb.gym_app.db.WorkoutWithExercises


@ExperimentalMaterial3Api
@Composable
fun BaseWorkoutDialog(
    confirmText: String,
    title: String,
    workout: WorkoutWithExercises?,
    onConfirm: (Workout) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(workout?.workout?.name ?: "") }
    var notes by remember { mutableStateOf(workout?.workout?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val data = Workout(
                        id = workout?.workout?.id ?: 0,
                        name = name,
                        notes = notes,
                    )
                    onConfirm(data)
                }
            ) {
                Text(confirmText)
            }
        },
        title = {
            Text(title)
        },
        text = {
            Column {
                TextField(
                    label = {
                        Text(stringResource(R.string.name).cap)
                    },
                    value = name,
                    onValueChange = { name = it }
                )
                TextField(
                    label = {
                        Text(stringResource(R.string.notes).cap)
                    },
                    value = notes,
                    onValueChange = { notes = it }
                )
            }
        }
    )
}