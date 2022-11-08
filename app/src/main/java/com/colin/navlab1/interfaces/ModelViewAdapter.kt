package com.colin.navlab1.interfaces

import com.colin.navlab1.model.Post

interface ModelViewAdapter {
    fun setDataset(posts: List<Post>)
    fun updateItem(post: Post, position: Int)
}