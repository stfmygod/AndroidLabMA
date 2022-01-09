package com.faculta.androidlabma.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.faculta.androidlabma.data.db.dao.MovieDAO
import com.faculta.androidlabma.data.db.model.MovieDB

@Database(entities = [MovieDB::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDAO(): MovieDAO

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "Movie database")
                .build()
    }
}