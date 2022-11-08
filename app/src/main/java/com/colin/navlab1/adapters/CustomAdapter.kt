package com.colin.navlab1.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.colin.navlab1.interfaces.ModelViewAdapter
import com.colin.navlab1.interfaces.NewsRowClickListener
import com.colin.navlab1.R
import com.colin.navlab1.model.Post

open class CustomAdapter(private val clickListener: NewsRowClickListener) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>(), ModelViewAdapter {

    protected var dataSet: MutableList<Post> = listOf<Post>().toMutableList()

    class ViewHolder(view: View, private val listener: NewsRowClickListener) : RecyclerView.ViewHolder(view),
        OnClickListener {
        val textView: TextView
        val textViewSmall: TextView
        val buttonLike: ImageButton

        override fun onClick(p0: View?) {
            listener.onPositionClicked(adapterPosition, p0!!)
        }

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
            textViewSmall = view.findViewById(R.id.textViewSmall)
            buttonLike = view.findViewById(R.id.imageButtonLike)
            buttonLike.setOnClickListener(this)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view, clickListener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if(dataSet[position].liked!!) {
            viewHolder.buttonLike.setImageDrawable(ContextCompat.getDrawable(viewHolder.buttonLike.context,
                R.drawable.heart_red
            ))
        } else {
            viewHolder.buttonLike.setImageDrawable(ContextCompat.getDrawable(viewHolder.buttonLike.context,
                R.drawable.heart
            ))
        }
        viewHolder.textView.text = dataSet[position].title + " " + dataSet[position].id
        viewHolder.textViewSmall.text = dataSet[position].body

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    override fun setDataset(posts: List<Post>) {
        dataSet.clear()
        dataSet = posts as MutableList<Post>
        notifyDataSetChanged()
    }

    override fun updateItem(post: Post, position: Int) {
        dataSet[position] = post
        notifyItemChanged(position)
    }

}
