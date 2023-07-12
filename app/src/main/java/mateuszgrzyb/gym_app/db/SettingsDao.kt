package mateuszgrzyb.gym_app.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun getOptional(): Flow<Settings?>

    @Query("SELECT * FROM settings")
    fun get(): Flow<Settings>

    @Query("SELECT * FROM settings")
    suspend fun getCoro(): Settings?

    @Insert
    suspend fun insert(settings: Settings): Long

    @Update
    suspend fun update(settings: Settings)
}