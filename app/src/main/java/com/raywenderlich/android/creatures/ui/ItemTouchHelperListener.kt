package com.raywenderlich.android.creatures.ui

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemSwipe(viewHolder: RecyclerView.ViewHolder, position: Int)
}