package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryPrediction::class], version = 1)
abstract class HistoryPredictionRoomDatabase : RoomDatabase() {
    abstract fun historyPredictionDao(): HistoryPredictionDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryPredictionRoomDatabase? = null

        fun getDatabase(context: Context): HistoryPredictionRoomDatabase {
            if (INSTANCE == null) {
                synchronized(HistoryPredictionRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        HistoryPredictionRoomDatabase::class.java, "classification_database")
                        .build()
                }
            }
            return INSTANCE as HistoryPredictionRoomDatabase
        }
    }
}