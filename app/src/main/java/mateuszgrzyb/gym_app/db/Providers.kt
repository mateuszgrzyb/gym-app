package mateuszgrzyb.gym_app.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Providers {
    @Provides
    @Singleton
    fun getDb(@ApplicationContext context: Context): DB =
        Room.databaseBuilder(context, DB::class.java, "gym-app").build()
}
