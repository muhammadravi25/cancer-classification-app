package com.dicoding.asclepius.helper

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.database.HistoryPrediction

class HistoryPredictionDiffCallback(private val oldHistoryPredictionList: List<HistoryPrediction>, private val newHistoryPredictionList: List<HistoryPrediction>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldHistoryPredictionList.size
    override fun getNewListSize(): Int = newHistoryPredictionList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryPredictionList[oldItemPosition].id == newHistoryPredictionList[newItemPosition].id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistoryPrediction = oldHistoryPredictionList[oldItemPosition]
        val newHistoryPrediction = newHistoryPredictionList[newItemPosition]
        return oldHistoryPrediction.prediction == newHistoryPrediction.prediction
                && oldHistoryPrediction.image == newHistoryPrediction.image
                && oldHistoryPrediction.confidence_score == newHistoryPrediction.confidence_score
    }
}