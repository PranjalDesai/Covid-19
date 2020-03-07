package com.pranjaldesai.coronavirustracker.ui.shared

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.R

class DividerItemDecoration(val context: Context) : RecyclerView.ItemDecoration() {

    private val attributes = IntArray(ATTRIBUTE_SIZE) { android.R.attr.listDivider }
    private var divider: Drawable? = null

    init {
        val styledAttributes = context.obtainStyledAttributes(attributes)
        divider = styledAttributes.getDrawable(DRAWABLE_INDEX)
        styledAttributes.recycle()
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, parent, state)
        divider?.let {
            canvas.save()
            val rightPadding = parent.width - parent.paddingRight
            for (childPosition in 0 until parent.childCount) {
                val child = parent.getChildAt(childPosition)
                val params = child.layoutParams as RecyclerView.LayoutParams

                val leftPadding = separateCategorizedItemDecoration(childPosition, parent, child)
                val topPadding = child.bottom + params.bottomMargin
                val bottomPadding = topPadding + it.intrinsicHeight

                it.setBounds(leftPadding, topPadding, rightPadding, bottomPadding)
                it.draw(canvas)
            }
            canvas.restore()
        }
    }

    private fun separateCategorizedItemDecoration(
        currentChildPosition: Int,
        parent: RecyclerView,
        child: View
    ): Int {
        val futureChildPosition = currentChildPosition + NEXT_ITEM
        return if (futureChildPosition < parent.childCount) {
            val futureChild = parent.getChildAt(futureChildPosition)
            if (futureChild.tag != child.tag) {
                NO_PADDING
            } else {
                generateLeftPadding(child.tag)
            }
        } else {
            NO_PADDING
        }
    }

    private fun generateLeftPadding(tag: Any?): Int {
        return if (tag != null) {
            val dimensionPixelOffset = when (tag) {
                else -> R.dimen.padding_none
            }
            context.resources.getDimensionPixelOffset(dimensionPixelOffset)
        } else {
            NO_PADDING
        }
    }

    companion object {
        private const val NO_PADDING = 0
        private const val DRAWABLE_INDEX = 0
        private const val NEXT_ITEM = 1
        private const val ATTRIBUTE_SIZE = 1
    }
}