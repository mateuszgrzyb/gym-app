package mateuszgrzyb.gym_app.ui.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel

@Composable
fun WorkoutsList(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    modifier: Modifier,
    content: @Composable (WorkoutWithExercises) -> Unit,
) {
    val workouts by workoutsViewModel.workouts.observeAsState(listOf())

    LazyColumn(modifier = modifier) {
        items(workouts) { workout ->
            content(workout)
        }
    }
}