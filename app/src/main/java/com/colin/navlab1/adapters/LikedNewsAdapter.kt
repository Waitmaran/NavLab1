package com.colin.navlab1.adapters

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.colin.navlab1.R
import com.colin.navlab1.interfaces.NewsRowClickListener


class LikedNewsAdapter(clickListener: NewsRowClickListener) : CustomAdapter(clickListener) {
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(dataSet[position].liked!!) {
            viewHolder.buttonLike.setImageDrawable(
                ContextCompat.getDrawable(viewHolder.buttonLike.context,
                R.drawable.heart_red
            ))
        } else {
            viewHolder.itemView.visibility = View.GONE
            viewHolder.itemView.layoutParams.height = 0
            viewHolder.itemView.layoutParams.width = 0
            //viewHolder.itemView.setLayoutParams(params)
        }
        viewHolder.textView.text = dataSet[position].title + " " + dataSet[position].id
        viewHolder.textViewSmall.text = dataSet[position].body
    }
}