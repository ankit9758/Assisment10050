package com.neostardemo.di

import android.content.Context
import com.neostardemo.NeoStarApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NeoStarModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): NeoStarApplication {
        return app as NeoStarApplication
    }
}