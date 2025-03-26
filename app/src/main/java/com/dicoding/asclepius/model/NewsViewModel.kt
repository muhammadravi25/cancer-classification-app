package com.dicoding.asclepius.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.response.ArticlesItem
import com.dicoding.asclepius.data.response.NewsResponse
import com.dicoding.asclepius.data.retrofit.ApiConfig
import com.dicoding.asclepius.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel: ViewModel() {
    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: LiveData<List<ArticlesItem>> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadRV = MutableLiveData<Boolean>()
    val isLoadRV: LiveData<Boolean> = _isLoadRV

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    companion object {
        private const val TAG = "NewsViewModel"
        private const val Q = "cancer"
        private const val CATEGORY = "health"
        private const val LANGUAGE = "en"
    }

    init {
        findNews(Q, CATEGORY, LANGUAGE)
    }

    fun findNews(q: String, category: String, language: String) {
        _isLoadRV.value = true
        _isLoading.value = true

        val client = ApiConfig.getApiService().getNews(q, category, language, BuildConfig.API_KEY)
        client.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(
                call: Call<NewsResponse>,
                response: Response<NewsResponse>
            ) {
                _isLoading.value = false
                _isLoadRV.value = false
                if (response.isSuccessful) {
                    _listNews.value = response.body()?.articles as List<ArticlesItem>?
                } else {
                    _snackbarText.value = Event("on Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                _isLoading.value = false
                _isLoadRV.value = false
                _snackbarText.value = Event("onFailure ${t.message.toString()}")
            }
        })
    }
}