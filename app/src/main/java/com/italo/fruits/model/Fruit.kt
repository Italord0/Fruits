package com.italo.fruits.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fruit(var name: String?, var benefits: String?, var image: String?) : Parcelable {

}