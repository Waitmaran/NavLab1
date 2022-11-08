package com.colin.navlab1.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.colin.navlab1.model.Post
import com.colin.navlab1.repositories.NewsRepository
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job

class NewsViewModel(private val repos: NewsRepository, owner: LifecycleOwner) : ViewModel() {
    private var _news = MutableLiveData<List<Post>>()
    val news: LiveData<List<Post>>
        get() = _news

    init {

        NewsRepository.news.observe(owner) {
            _news.value = it
        }
    }

    private lateinit var job: Job

    fun likeNews(position: Int): Post {
        val likeInverse = _news.value?.get(position)?.liked?.not()
        _news.value?.get(position)?.liked = likeInverse
        val database = Firebase.database("https://acquired-talent-349123-default-rtdb.europe-west1.firebasedatabase.app/").reference
        if(likeInverse!!) {
            database.child("likedPosts").child(_news.value?.get(position)?.id.toString()).setValue(_news.value?.get(position))
        } else {
            database.child("likedPosts").child(_news.value?.get(position)?.id.toString()).setValue(null)
        }

        return _news.value?.get(position)!!
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized)
            job.cancel()
    }
}