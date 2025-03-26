package com.dicoding.asclepius.model

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.HistoryPrediction
import com.dicoding.asclepius.repository.HistoryPredictionRepository

class HistoryPredictionAddViewModel(application: Application) : ViewModel() {
    private val mHistoryPredictionRepository: HistoryPredictionRepository = HistoryPredictionRepository(application)

    fun insert(historyPrediction: HistoryPrediction) {
        mHistoryPredictionRepository.insert(historyPrediction)
    }
}