package com.colin.navlab1.interfaces

import android.view.View
import com.google.firebase.firestore.DocumentSnapshot

interface DeleteNoteClickListener {
    fun onPositionClicked(pos: Int, view: View)
}
