package com.italo.fruits.callback

import androidx.recyclerview.widget.RecyclerView

interface FruitRecyclerViewCallback {

    fun onSwipe(position: Int , view : RecyclerView.ViewHolder)

    fun onMoved(initPosition : Int, targetPosition : Int)

}