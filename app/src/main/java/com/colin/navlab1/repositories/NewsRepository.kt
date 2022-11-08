package com.colin.navlab1.repositories

import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.colin.navlab1.common.Common
import com.colin.navlab1.retrofit.SafeApiRequest
import com.colin.navlab1.model.Post

object NewsRepository: SafeApiRequest() {

     private var _loading = MutableLiveData<Boolean>()
     val loading: LiveData<Boolean>
          get() = _loading

     private var _news = MutableLiveData<List<Post>>()
     val news: LiveData<List<Post>>
          get() = _news

     fun setLoading() {
          _loading.value = true
     }

     fun setNews(newsToSet: List<Post>) {
          Log.d("REPOS", "SETTING NEWS")
          _news.value = newsToSet
          _loading.value = false
     }

     suspend fun getNews(): List<Post> {
          Log.d("REPOS", "GETTING NEWS")
          return apiRequest { Common.retrofitService.getPostsList() }
     }

}