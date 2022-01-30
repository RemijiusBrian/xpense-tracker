package com.ridill.xpensetracker.di

import android.app.Application
import androidx.room.Room
import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManager
import com.ridill.xpensetracker.core.data.preferences.AppPreferencesManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): XTDatabase =
        Room.databaseBuilder(
            application,
            XTDatabase::class.java,
            XTDatabase.NAME
        ).fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun provideAppPreferencesManager(application: Application): AppPreferencesManager =
        AppPreferencesManagerImpl(application)
}