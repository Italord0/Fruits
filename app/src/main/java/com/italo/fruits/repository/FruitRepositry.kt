package com.italo.fruits.repository

interface FruitRepositry {

    suspend fun insert()

    suspend fun delete()

    suspend fun list()

}