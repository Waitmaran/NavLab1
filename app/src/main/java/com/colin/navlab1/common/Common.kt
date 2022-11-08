package com.colin.navlab1.common

import android.app.NotificationManager
import androidx.core.content.ContextCompat.getSystemService
import com.colin.navlab1.interfaces.RetrofitServices
import com.colin.navlab1.retrofit.RetrofitClient

object Common {
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"
    val retrofitService: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}