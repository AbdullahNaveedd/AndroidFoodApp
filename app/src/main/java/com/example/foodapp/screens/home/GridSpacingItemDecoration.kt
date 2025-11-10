package com.example.foodapp.screens.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(
    private val spanCount: Int,
    private val horizontalSpace: Int,
    private val verticalSpace: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = column * horizontalSpace / spanCount
        outRect.right = horizontalSpace - (column + 1) * horizontalSpace / spanCount

        if (position >= spanCount) {
            outRect.top = verticalSpace
        }
    }
}
