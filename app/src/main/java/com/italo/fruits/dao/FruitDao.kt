package com.italo.fruits.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.italo.fruits.model.Fruit

@Dao
interface FruitDao {

    @Query("SELECT * FROM fruit")
    fun getAll(): List<Fruit>

    @Insert
    fun insert(fruit: Fruit)

    @Delete
    fun delete(fruit: Fruit)

}