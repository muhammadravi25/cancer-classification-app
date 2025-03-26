package com.dicoding.asclepius.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.HistoryPredictionAdapter
import com.dicoding.asclepius.databinding.ActivityHistoryPredictionBinding
import com.dicoding.asclepius.model.HistoryPredictionViewModel
import com.dicoding.asclepius.model.ViewModelFactory

class HistoryPredictionActivity : AppCompatActivity() {

    private var _activityHistoryPredictionBinding: ActivityHistoryPredictionBinding? = null
    private val binding get() = _activityHistoryPredictionBinding

    private lateinit var adapter: HistoryPredictionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityHistoryPredictionBinding = ActivityHistoryPredictionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        adapter = HistoryPredictionAdapter()
        binding?.rvHistoryPredict?.layoutManager = LinearLayoutManager(this)
        binding?.rvHistoryPredict?.adapter = adapter

        val historyPredictionViewModel = obtainViewModel(this@HistoryPredictionActivity)
        historyPredictionViewModel.getAllHistoryPrediction().observe(this) { historyPredictionList ->
            if (historyPredictionList != null) {
                adapter.setListHistoryPrediction(historyPredictionList)
                binding?.resultText?.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_history -> {
                val moveToHistoryPredictionIntent = Intent(this@HistoryPredictionActivity, HistoryPredictionActivity::class.java)
                startActivity(moveToHistoryPredictionIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityHistoryPredictionBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryPredictionViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryPredictionViewModel::class.java)
    }
}