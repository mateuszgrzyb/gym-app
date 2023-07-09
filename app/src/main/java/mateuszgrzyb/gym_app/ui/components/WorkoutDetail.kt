package mateuszgrzyb.gym_app.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.db.WorkoutWithExercises

@Composable
fun WorkoutDetail(
    workout: WorkoutWithExercises,
    modifier: Modifier,
    content: @Composable (Exercise) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(workout.exercises) { exercise ->
            content(exercise)
        }
    }
}

