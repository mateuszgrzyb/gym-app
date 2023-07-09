package mateuszgrzyb.gym_app.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.displayName
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel

@Composable
fun WorkoutsList(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    modifier: Modifier,
) {
    val workouts = workoutsViewModel.workouts
    LazyColumn(modifier = modifier) {
        items(workouts) {
            Card(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .clickable {
                        workoutsViewModel.setCurrentWorkout(it.workout.id)
                    },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {
                    Text(displayName(it.workout))
                }
            }
        }
    }
}