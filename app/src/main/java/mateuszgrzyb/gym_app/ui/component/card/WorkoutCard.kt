package mateuszgrzyb.gym_app.ui.component.card

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import mateuszgrzyb.gym_app.displayName
import mateuszgrzyb.gym_app.ui.component.dialog.UpdateWorkoutDialog
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel

@ExperimentalMaterial3Api
@Composable
fun WorkoutCard(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    workout: WorkoutWithExercises,
) {
    var editable by remember { mutableStateOf(false) }

    if (editable) {
        UpdateWorkoutDialog(
            workout = workout,
            onConfirm = {
                Log.d("asdf", "$it")
                workoutsViewModel.updateWorkout(it)
                editable = false
            },
            onDismiss = {
                editable = false
            }
        )
    }

    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        workoutsViewModel.currentWorkoutId = workout.workout.id
                    },
                    onLongPress = {
                        editable = true
                    }
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
        ) {
            Text(displayName(workout.workout), fontWeight = FontWeight.Bold)
            if (workout.workout.notes?.isNotBlank() == true) {
                Text(workout.workout.notes)
            }
        }
    }
}