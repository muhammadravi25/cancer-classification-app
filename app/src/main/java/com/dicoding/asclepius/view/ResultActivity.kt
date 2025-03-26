package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.adapter.NewsAdapter
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.database.HistoryPrediction
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.model.HistoryPredictionAddViewModel
import com.dicoding.asclepius.model.ViewModelFactory
import com.dicoding.asclepius.model.NewsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var historyPredictionAddViewModel: HistoryPredictionAddViewModel
    private val newsViewModel by viewModels<NewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvArticle.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this
        , layoutManager.orientation)
        binding.rvArticle.addItemDecoration(itemDecoration)

        historyPredictionAddViewModel = obtainViewModel(this@ResultActivity)

        val confidenceScore = intent.getStringExtra(CONFIDENCESCORE)
        val labelPredict = intent.getStringExtra(LABEL_PREDICT)
        val imagePredictUri: Uri? = intent.getParcelableExtra(IMAGE_PREDICT)

        if (confidenceScore != null && imagePredictUri != null) {
            binding.resultImage.setImageURI(imagePredictUri)
            binding.resultScore.text = confidenceScore
            binding.resultPredict.text = labelPredict

            val newHistoryPrediction = HistoryPrediction().apply {
                prediction = labelPredict
                confidence_score = confidenceScore
                image = imagePredictUri.toString()
            }

            CoroutineScope(Dispatchers.IO).launch {
                historyPredictionAddViewModel.insert(newHistoryPrediction)
            }
            newsViewModel.listNews.observe(this) {listNews ->
                setListData(listNews)
            }

            newsViewModel.isLoadRV.observe(this) {
                showRecycleView(it)
            }

            newsViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        } else {
            Toast.makeText(this, "Predict failed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setListData(listNews: List<ArticlesItem>){
        val adapter = NewsAdapter(object : NewsAdapter.OnItemClickCallback {
            override fun onItemClicked(news: ArticlesItem) {
                showSelectedNews(news)
            }
        })
        adapter.submitList(listNews)
        binding.rvArticle.adapter = adapter
    }

    private fun obtainViewModel(activity: AppCompatActivity): HistoryPredictionAddViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HistoryPredictionAddViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_history -> {
                val moveToHistoryPredictionIntent = Intent(this@ResultActivity, HistoryPredictionActivity::class.java)
                startActivity(moveToHistoryPredictionIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecycleView(isLoadRV: Boolean) {
        binding.rvArticle.visibility = if (isLoadRV) View.GONE else View.VISIBLE
    }

    private fun showSelectedNews(item: ArticlesItem) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
        startActivity(intent)
    }

    companion object {
        const val CONFIDENCESCORE = "confidence_score"
        const val LABEL_PREDICT = "label_predict"
        const val IMAGE_PREDICT = "image_predict"
    }
}
