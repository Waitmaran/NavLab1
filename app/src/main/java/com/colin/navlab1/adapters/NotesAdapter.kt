package com.colin.navlab1.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.view.View.OnClickListener
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.colin.navlab1.R
import com.colin.navlab1.interfaces.DeleteNoteClickListener
import com.colin.navlab1.model.Note
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject

class NotesAdapter(private val clickListener: DeleteNoteClickListener) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var dataSet: MutableList<Note> = listOf<Note>().toMutableList()

    class ViewHolder(view: View, private val listener: DeleteNoteClickListener) : RecyclerView.ViewHolder(view),
        OnClickListener {

        val deleteButton: Button
        val textView: TextView

        override fun onClick(p0: View?) {
            listener.onPositionClicked(adapterPosition, p0!!)
        }

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textViewNote)
            deleteButton = view.findViewById(R.id.buttonDelete)
            deleteButton.setOnClickListener(this)
        }
    }

    fun getNote(position: Int): Note {
        return dataSet[position]
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(ds: MutableList<Note>) {
        dataSet.clear()
        dataSet = ds
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].body
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.note_row_item, parent, false)

        return ViewHolder(view, clickListener)
    }

}
