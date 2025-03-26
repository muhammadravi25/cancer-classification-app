package com.dicoding.asclepius.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.database.HistoryPrediction
import com.dicoding.asclepius.databinding.ItemCardBinding
import com.dicoding.asclepius.helper.HistoryPredictionDiffCallback

class HistoryPredictionAdapter : RecyclerView.Adapter<HistoryPredictionAdapter.HistoryPredictionViewHolder>() {
    private val listHistoryPrediction = ArrayList<HistoryPrediction>()

    fun setListHistoryPrediction(listHistoryPrediction: List<HistoryPrediction>) {
        val diffCallback = HistoryPredictionDiffCallback(this.listHistoryPrediction, listHistoryPrediction)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listHistoryPrediction.clear()
        this.listHistoryPrediction.addAll(listHistoryPrediction)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryPredictionViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryPredictionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryPredictionViewHolder, position: Int) {
        holder.bind(listHistoryPrediction[position])
    }

    override fun getItemCount(): Int {
        return listHistoryPrediction.size
    }

    inner class HistoryPredictionViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(historyPrediction: HistoryPrediction) {
            with(binding) {
                tvItemTitle.text = "${historyPrediction.prediction}"
                tvItemSecondary.text = "${historyPrediction.confidence_score}"
                Glide.with(binding.root.context)
                    .load(historyPrediction.image)
                    .into(binding.imgItemThumbnail)
            }
        }
    }
}