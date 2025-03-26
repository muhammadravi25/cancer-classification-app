package com.dicoding.asclepius.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.database.HistoryPrediction
import com.dicoding.asclepius.repository.HistoryPredictionRepository

class HistoryPredictionViewModel(application: Application) : ViewModel() {
    private val mHistoryPredictionRepository: HistoryPredictionRepository = HistoryPredictionRepository(application)
    fun getAllHistoryPrediction(): LiveData<List<HistoryPrediction>> = mHistoryPredictionRepository.getAllHistoryPrediction()
}