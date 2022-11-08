package com.colin.navlab1.mvvm

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.colin.navlab1.repositories.NewsRepository
import com.colin.navlab1.interfaces.ModelViewAdapter

@Suppress("UNCHECKED_CAST")
class NewsViewModelFactory(private val repos: NewsRepository, private val owner: LifecycleOwner): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return NewsViewModel(repos, owner) as T
    }

}