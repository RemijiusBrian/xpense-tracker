package com.ridill.xpensetracker.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.ridill.xpensetracker.core.data.local.db.XTDatabase
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManager
import com.ridill.xpensetracker.core.data.preferences.XTPreferencesManagerImpl
import com.ridill.xpensetracker.core.util.DispatcherProvider
import com.ridill.xpensetracker.core.util.DispatcherProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideXTDatastore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.preferencesDataStoreFile(XT_PREFERENCES) },
            migrations = listOf()
        )

    @Singleton
    @Provides
    fun provideAppPreferencesManager(
        dataStore: DataStore<Preferences>,
        dispatcherProvider: DispatcherProvider
    ): XTPreferencesManager = XTPreferencesManagerImpl(dataStore, dispatcherProvider)

    @Provides
    fun provideDispatcherProvider(): DispatcherProvider = DispatcherProviderImpl()
}

private const val XT_PREFERENCES = "XT_PREFERENCES"