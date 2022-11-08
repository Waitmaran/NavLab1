package com.colin.navlab1.interfaces

import android.view.View
import android.view.View.OnClickListener

interface NewsRowClickListener {
    fun onPositionClicked(position: Int, view: View)
}