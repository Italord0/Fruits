package com.italo.fruits.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.italo.fruits.R
import com.italo.fruits.callback.FruitRecyclerViewCallback

class FruitItemTouchHelperCallback(val context: Context,private val callback: FruitRecyclerViewCallback) :
    ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var deleteBackground: ColorDrawable = ColorDrawable(ContextCompat.getColor(context, R.color.delete_red))
    private lateinit var deleteIcon : Drawable

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        callback.onMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        callback.onSwipe(viewHolder.adapterPosition, viewHolder)
        callback
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete)!!

        val itemView = viewHolder.itemView

        val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

        if (dX > 0) {
            deleteBackground.setBounds(itemView.left, itemView.top, dX.toInt(), itemView.bottom)
            deleteIcon.setBounds(itemView.left + iconMargin,itemView.top + iconMargin,itemView.left + iconMargin + deleteIcon.intrinsicWidth , itemView.bottom - iconMargin )
        } else {
            deleteBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth,itemView.top + iconMargin,itemView.right - iconMargin , itemView.bottom - iconMargin )
        }

        deleteBackground.draw(c)
        deleteIcon.draw(c)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}