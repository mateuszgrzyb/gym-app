package mateuszgrzyb.gym_app.ui.component

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable


@Composable
fun WorkoutDetail(
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    //workout: WorkoutWithExercises,
    modifier: Modifier,
    content: @Composable (Exercise) -> Unit,
) {
    val workout by workoutsViewModel.currentWorkout.observeAsState()

    if (workout == null) {
        return
    }

    LazyColumn(modifier = modifier) {
        items(workout!!.sortedExercises) { exercise ->
            content(exercise)
        }
    }
}


@Composable
fun ReorderWorkoutDetail(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    //workout: WorkoutWithExercises,
    modifier: Modifier,
    content: @Composable (Exercise, Boolean) -> Unit,
) {
    val workout by workoutsViewModel.currentWorkout.observeAsState()

    if (workout == null) {
        return
    }

    // var exercises by remember { mutableStateOf(workout!!.exercises) }
    var exercises by remember { mutableStateOf(workout!!.sortedExercises) }

    val state = rememberReorderableLazyListState(onMove = { from, to ->
        exercises = exercises.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyColumn(
        state = state.listState,
        modifier = modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        items(exercises, { it.id }) { item ->
            ReorderableItem(state, key = item.id) { isDragging ->
                content(item, isDragging)
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            val updatedExercises = exercises.mapIndexed { i, e ->
                e.copy(ordering = i + 1)
            }

            val diff = updatedExercises.toSet() - workout!!.exercises.toSet()

            workoutsViewModel.updateExercises(*diff.toTypedArray())
        }
    }
}
