package com.raywenderlich.android.creatures.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView

class DividerItemDecoration(color: Int, private val heightInPixels: Int) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 0F
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + heightInPixels
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}