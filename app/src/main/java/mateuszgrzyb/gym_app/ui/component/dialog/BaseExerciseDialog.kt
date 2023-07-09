package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.WeightUnit
import mateuszgrzyb.gym_app.cap
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.ui.component.DropdownTextField

@ExperimentalMaterial3Api
@Composable
fun BaseExerciseDialog(
    confirmText: String,
    title: String,
    exercise: Exercise?,
    workoutId: Long,
    onConfirm: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(exercise?.name ?: "") }
    var sets by remember { mutableStateOf(exercise?.sets?.toString() ?: "") }
    var reps by remember { mutableStateOf(exercise?.reps?.toString() ?: "") }
    var weightValue by remember { mutableStateOf(exercise?.weightValue?.toString() ?: "") }
    var weightUnit by remember { mutableStateOf(exercise?.weightUnit ?: WeightUnit.KG) }

    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val data = Exercise(
                    id = exercise?.id ?: 0,
                    workoutId = workoutId,
                    name = name,
                    sets = sets.toInt(),
                    reps = reps.toInt(),
                    weightValue = weightValue.toDouble(),
                    weightUnit = weightUnit,
                )
                onConfirm(data)
            }) {
                Text(confirmText)
            }
        },
        title = {
            Text(title)
        },
        text = {
            Column {
                DropdownTextField(
                    label = {
                        Text(stringResource(R.string.name).cap)
                    },
                    field = name,
                    setField = { name = it },
                )
                TextField(
                    label = {
                        Text(stringResource(R.string.sets).cap)
                    },
                    value = sets,
                    onValueChange = { sets = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                TextField(
                    label = {
                        Text(stringResource(R.string.reps).cap)
                    },
                    value = reps,
                    onValueChange = { reps = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
                TextField(
                    label = {
                        Text(stringResource(R.string.weight).cap)
                    },
                    value = weightValue,
                    onValueChange = { weightValue = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(onClick = { expanded = true }) {
                    Text("$weightUnit")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(
                        text = { Text("KG") },
                        onClick = {
                            weightUnit = WeightUnit.KG
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("LB") },
                        onClick = {
                            weightUnit = WeightUnit.LB
                            expanded = false
                        }
                    )
                }
            }
        }
    )
}