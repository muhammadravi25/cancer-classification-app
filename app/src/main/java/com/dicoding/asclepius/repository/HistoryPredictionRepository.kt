package com.dicoding.asclepius.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.HistoryPrediction
import com.dicoding.asclepius.database.HistoryPredictionDao
import com.dicoding.asclepius.database.HistoryPredictionRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryPredictionRepository(application: Application) {
    private val mHistoryPredictionDao: HistoryPredictionDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryPredictionRoomDatabase.getDatabase(application)
        mHistoryPredictionDao = db.historyPredictionDao()
    }

    fun getAllHistoryPrediction(): LiveData<List<HistoryPrediction>> = mHistoryPredictionDao.getAllHistoryPrediction()

    fun insert(historyPrediction: HistoryPrediction) {
        executorService.execute { mHistoryPredictionDao.insert(historyPrediction) }
    }
}