package mateuszgrzyb.gym_app.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "settings"
)
data class Settings(
    @PrimaryKey(autoGenerate = true) var id: Long
) {
    constructor(): this(
        id = 0L
    )
}
