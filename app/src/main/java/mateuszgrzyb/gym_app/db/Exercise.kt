package mateuszgrzyb.gym_app.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import mateuszgrzyb.gym_app.WeightUnit

@Entity(
    tableName = "exercise",
    foreignKeys = [
        ForeignKey(
            entity = Workout::class,
            parentColumns = ["id"],
            childColumns = ["workout_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("workout_id")],
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "workout_id") val workoutId: Long,
    val name: String,
    val sets: Int,
    val reps: Int,
    @ColumnInfo(name = "weight_value") val weightValue: Double,
    @ColumnInfo(name = "weight_unit") val weightUnit: WeightUnit,
)