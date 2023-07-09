package mateuszgrzyb.gym_app.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settings")
    fun get(): Flow<Settings>

    @Upsert
    suspend fun upsert(settings: Settings)
}