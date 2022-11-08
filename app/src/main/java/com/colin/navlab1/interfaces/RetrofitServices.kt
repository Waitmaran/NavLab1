package com.colin.navlab1.interfaces
import com.colin.navlab1.model.Post
import retrofit2.Response
import retrofit2.http.*

interface RetrofitServices {
    @GET("posts")
    suspend fun getPostsList(): Response<List<Post>>
}