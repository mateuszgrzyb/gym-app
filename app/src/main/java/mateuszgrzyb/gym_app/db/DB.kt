package mateuszgrzyb.gym_app.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database(entities = [Workout::class, Exercise::class, Settings::class], version = 1)
abstract class DB : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun settingsDao(): SettingsDao
}