package com.neostardemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.neostardemo.models.User
import com.neostardemo.utils.AppConstants

@Database(entities = [User::class], version = 1)
abstract class NeoStarDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    companion object {
        @Volatile
        private var instance: NeoStarDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            NeoStarDatabase::class.java,
            AppConstants.DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

}