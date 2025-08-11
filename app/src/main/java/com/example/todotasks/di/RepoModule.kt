package com.example.todotasks.di

import com.example.todotasks.database.dao.TaskDao
import com.example.todotasks.repository.TaskRepo
import com.example.todotasks.repository.TaskRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Singleton
    @Provides
    fun provideTaskRepo(taskDao: TaskDao): TaskRepo {
        // Replace with actual repository initialization logic
        return TaskRepoImpl(taskDao)
    }
}