package mateuszgrzyb.gym_app.viewmodels

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
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

    private val workoutsFlow = db.workoutDao().getAll()
    val workouts = workoutsFlow.asLiveData()

    private var currentWorkoutId by mutableStateOf<Long?>(null)
    private val _currentWorkout = MutableLiveData<WorkoutWithExercises?>(null)
    val currentWorkout = _currentWorkout as LiveData<WorkoutWithExercises?>

    private val _nextExerciseOrdering = MutableLiveData(0)
    val nextExerciseOrdering = _nextExerciseOrdering as LiveData<Int>

    init {
        viewModelScope.launch {
            val currentWorkoutIdFlow = snapshotFlow { currentWorkoutId };

            currentWorkoutIdFlow.combine(workoutsFlow, ::Pair).collect { (newId, ws) ->
                for (w in ws) {
                    if (w.workout.id != newId) {
                        continue
                    }

                    _currentWorkout.value = w
                    _nextExerciseOrdering.value = (w.sortedExercises.lastOrNull()?.ordering ?: 0) + 1
                }
            }
        }
    }

    fun setCurrentWorkout(id: Long?) {
        currentWorkoutId = id
    }

    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            currentWorkoutId = db.workoutDao().insert(workout)
        }
    }

    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            db.exerciseDao().insert(exercise)
        }
    }

    fun updateWorkout(workout: Workout) {
        viewModelScope.launch {
            db.workoutDao().update(workout)
        }
    }

    fun updateExercises(vararg exercises: Exercise) {
        viewModelScope.launch {
            db.exerciseDao().update(*exercises)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            db.workoutDao().delete(workout)
            currentWorkoutId = null
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            db.exerciseDao().delete(exercise)
        }
    }
}