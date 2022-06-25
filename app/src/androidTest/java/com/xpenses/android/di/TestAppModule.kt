package com.xpenses.android.di

import android.content.Context
import androidx.room.Room
import com.xpenses.android.core.data.local.db.XTDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideTestDb(@ApplicationContext context: Context): XTDatabase =
        Room.inMemoryDatabaseBuilder(context, XTDatabase::class.java)
            .allowMainThreadQueries()
            .build()
}