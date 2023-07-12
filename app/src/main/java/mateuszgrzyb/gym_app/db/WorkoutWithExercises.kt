package mateuszgrzyb.gym_app.db

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "id",
        entityColumn = "workout_id"
    )
    val exercises: List<Exercise>
) {
    val sortedExercises: List<Exercise>
        get() = exercises.sortedBy { it.ordering }
}