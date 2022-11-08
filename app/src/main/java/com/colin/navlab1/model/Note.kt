package com.colin.navlab1.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Note {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("body")
    @Expose
    var body: String? = null
}