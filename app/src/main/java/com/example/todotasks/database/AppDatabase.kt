package com.example.todotasks.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todotasks.database.dao.TaskDao
import com.example.todotasks.database.entity.TaskCollection
import com.example.todotasks.database.entity.TaskEntity

private const val DATABASE_NAME = "tasks_db"
private const val DATABASE_VERSION = 2

@Database(entities = [TaskEntity::class, TaskCollection::class], version = DATABASE_VERSION, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .addMigrations(MIGRATE_1_2).build()
    }
}

private val MIGRATE_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task_collection ADD COLUMN sort_type INTEGER NOT NULL DEFAULT 0")
    }
}