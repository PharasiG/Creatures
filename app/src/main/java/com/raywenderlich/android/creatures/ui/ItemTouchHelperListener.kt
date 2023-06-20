package com.raywenderlich.android.creatures.ui

import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onItemMove(recyclerView: RecyclerView, start: Int, end: Int): Boolean
//    fun onItemSwipe(recyclerView: RecyclerView)
}