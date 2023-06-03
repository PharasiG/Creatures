package com.raywenderlich.android.creatures.ui

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class FoodDividerItemDecoration(color: Int, private val height: Int, private val spanCount: Int) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint()

    init {
        paint.color = color
        paint.isAntiAlias = true
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val top = child.top
            val bottom = child.bottom
            val left = child.left
            val right = child.right

            c.drawRect(
                (left - parent.paddingStart).toFloat(),
                top.toFloat(),
                (right + parent.paddingEnd).toFloat(),
                (top + height).toFloat(),
                paint
            )
            if ((i + 1) % spanCount != 0) c.drawRect(
                right.toFloat(), top.toFloat(), (right + height).toFloat(), bottom.toFloat(), paint
            )
        }
    }
}