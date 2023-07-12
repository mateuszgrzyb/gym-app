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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.WeightUnit
import mateuszgrzyb.gym_app.cap
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.ui.component.DropdownTextField
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel

@ExperimentalMaterial3Api
@Composable
fun BaseExerciseDialog(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    confirmText: String,
    title: String,
    exercise: Exercise?,
    workoutId: Long,
    onConfirm: (Exercise) -> Unit,
    onDismiss: () -> Unit,
) {
    val nextExerciseOrdering by workoutsViewModel.nextExerciseOrdering.observeAsState(0)
    val ordering = exercise?.ordering ?: nextExerciseOrdering
    var name by remember { mutableStateOf(exercise?.name ?: "") }
    var sets by remember { mutableStateOf(exercise?.sets?.toString() ?: "") }
    var reps by remember { mutableStateOf(exercise?.reps?.toString() ?: "") }
    var weightValue by remember { mutableStateOf(exercise?.weightValue?.toString() ?: "") }

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
                    ordering = ordering,
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
            }
        }
    )
}