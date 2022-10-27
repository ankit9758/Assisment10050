package com.neostardemo.di

import com.neostardemo.database.UserDao
import com.neostardemo.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {
    @Provides
    @ActivityRetainedScoped
    fun provideMainRepository(userDao: UserDao): MainRepository =
        MainRepository(userDao)
}