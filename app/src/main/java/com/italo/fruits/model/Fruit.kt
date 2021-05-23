package com.italo.fruits.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Fruit(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var name: String?,
    var benefits: String?,
    var image: String?,
    var order: Int?
) : Parcelable