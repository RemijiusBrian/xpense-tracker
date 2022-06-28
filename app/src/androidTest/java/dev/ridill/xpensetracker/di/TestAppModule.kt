package dev.ridill.xpensetracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ridill.xpensetracker.core.data.local.db.XTDatabase
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