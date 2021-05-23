package com.italo.fruits.dao

import androidx.room.*
import com.italo.fruits.model.Fruit

@Dao
interface FruitDao {

    @Query("SELECT * FROM fruit  ORDER BY LOWER(`order`) ASC ")
    fun getAll(): List<Fruit>

    @Query("SELECT COUNT(id) FROM fruit WHERE name = :name COLLATE NOCASE ")
    fun getByName(name : String): Int

    @Insert
    fun insert(fruit: Fruit)

    @Delete
    fun delete(fruit: Fruit)

    @Query("SELECT COUNT(id) FROM fruit")
    fun count(): Int

    @Query("SELECT MAX(`order`) FROM fruit")
    fun order(): Int

    @Update
    fun update(fruit: Fruit);

}