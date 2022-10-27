package com.neostardemo.di

import android.content.Context
import com.neostardemo.database.NeoStarDatabase
import com.neostardemo.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): NeoStarDatabase {
        return NeoStarDatabase(context)
    }

    @Provides
    @Singleton
    fun provideIssuesDao(appDatabase:NeoStarDatabase ):UserDao{
        return appDatabase.getUserDao()
    }

}