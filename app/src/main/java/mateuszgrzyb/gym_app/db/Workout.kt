package mateuszgrzyb.gym_app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String?,
)