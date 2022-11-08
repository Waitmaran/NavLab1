package com.colin.navlab1.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Post {
    @SerializedName("userId")
    @Expose
    var userId: Int? = null

    @SerializedName("id")
    @Expose
    var id: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null

    var liked: Boolean? = false
}