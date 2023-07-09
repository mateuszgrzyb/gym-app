package mateuszgrzyb.gym_app.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

    val workouts = db.workoutDao().getAll().asLiveData()

    var currentWorkoutId by mutableStateOf<Long?>(null)

    suspend fun addWorkout(workout: Workout): Long {
        return db.workoutDao().insert(workout)
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

    fun updateExercise(exercise: Exercise) {
        viewModelScope.launch {
            db.exerciseDao().update(exercise)
        }
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            db.workoutDao().delete(workout)
        }
    }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            db.exerciseDao().delete(exercise)
        }
    }
}