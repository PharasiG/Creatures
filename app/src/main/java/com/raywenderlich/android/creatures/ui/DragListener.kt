package com.raywenderlich.android.creatures.ui

import androidx.recyclerview.widget.RecyclerView

interface DragListener {
    fun onDragged(viewHolder: RecyclerView.ViewHolder)
}