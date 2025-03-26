package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface HistoryPredictionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(historyPrediction: HistoryPrediction)

    @Query("SELECT * from HistoryPrediction ORDER BY id ASC")
    fun getAllHistoryPrediction(): LiveData<List<HistoryPrediction>>
}