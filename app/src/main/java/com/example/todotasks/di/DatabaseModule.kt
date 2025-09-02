package com.example.todotasks.di

import android.content.Context
import com.example.todotasks.database.AppDatabase
import com.example.todotasks.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        // Replace with actual DAO initialization logic
        return appDatabase.taskDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        // Replace with actual database initialization logic
        return AppDatabase.invoke(context)
    }

}