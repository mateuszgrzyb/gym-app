package mateuszgrzyb.gym_app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.db.DB
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.db.Workout
import mateuszgrzyb.gym_app.db.WorkoutWithExercises
import javax.inject.Inject

@HiltViewModel
class WorkoutsViewModel @Inject constructor(
    private val db: DB
): ViewModel() {
    var workouts by mutableStateOf(listOf<WorkoutWithExercises>())
        private set

    init {
        viewModelScope.launch {
            db.workoutDao().getAll().collectLatest { ws ->
                workouts = ws
            }
        }
    }

    var currentWorkout by mutableStateOf<WorkoutWithExercises?>(null)
        private set

    fun setCurrentWorkout(workoutId: Long?) {
        currentWorkout = workouts.find { it.workout.id == workoutId }
    }

    suspend fun add(workout: Workout): Long =
        db.workoutDao().insert(workout)

    suspend fun addExercise(exercise: Exercise): Long {
        exercise.id = db.exerciseDao().insert(exercise)

        workouts = workouts.map { w ->
            if (w.workout.id != exercise.workoutId) {
                w
            } else {
                WorkoutWithExercises(
                    workout = w.workout,
                    exercises = w.exercises + exercise
                )
            }
        }

        return exercise.id
    }

    suspend fun deleteExercise(exercise: Exercise) {
        db.exerciseDao().delete(exercise)

        workouts = workouts.map { w ->
            if (w.workout.id != exercise.workoutId) {
                w
            } else {
                WorkoutWithExercises(
                    workout = w.workout,
                    exercises = w.exercises.filter { it.id != exercise.id }
                )
            }
        }
    }

    suspend fun deleteWorkout(workout: Workout) {
        db.workoutDao().delete(workout)

        workouts = workouts.filter { it.workout.id == workout.id }
    }
}