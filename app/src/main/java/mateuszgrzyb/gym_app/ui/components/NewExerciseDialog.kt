package mateuszgrzyb.gym_app.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.WeightUnit
import mateuszgrzyb.gym_app.cap
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.viewmodels.ContextViewModel

@ExperimentalMaterial3Api
@Composable
fun DropdownTextField(
    contextViewModel: ContextViewModel = viewModel(),
    label: @Composable () -> Unit,
    field: String,
    setField: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = field,
            onValueChange = setField,
            label = label,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        // filter options based on text field value

        val filteringOptions = contextViewModel.fileContents.filter {it.contains(field, ignoreCase = true) }.take(10)

        if (filteringOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                filteringOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        onClick = {
                            setField(selectionOption)
                            expanded = false
                        },
                        text = {
                            Text(text = selectionOption)
                        }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun NewExerciseDialog(
    workoutId: Long?,
    onConfirm: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weightValue by remember { mutableStateOf("") }
    var weightUnit by remember { mutableStateOf(WeightUnit.KG) }

    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val data = Exercise(
                    id = 0,
                    workoutId = workoutId ?: 0,
                    name = name,
                    sets = sets.toInt(),
                    reps = reps.toInt(),
                    weightValue = weightValue.toDouble(),
                    weightUnit = weightUnit,
                )
                onConfirm(data)
            }) {
                Text(stringResource(R.string.create))
            }
        },
        title = {
            Text(stringResource(R.string.create_exercise_title))
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
                    Text("${weightUnit}")
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