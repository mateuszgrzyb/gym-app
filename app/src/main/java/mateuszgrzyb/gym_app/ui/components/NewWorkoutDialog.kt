package mateuszgrzyb.gym_app.ui.components

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

@ExperimentalMaterial3Api
@Composable
fun NewWorkoutDialog(
    onConfirm: (Workout) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    val data = Workout(
                        id = 0,
                        name = name,
                    )
                    onConfirm(data)
                }
            ) {
                Text(stringResource(R.string.create))
            }
        },
        title = {
            Text(stringResource(R.string.create_exercise_title))
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
            }
        }
    )
}