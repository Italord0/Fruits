package com.italo.fruits.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.italo.fruits.dao.FruitDao
import com.italo.fruits.model.Fruit

@Database(entities = [Fruit::class], version = 1)
abstract class FruitDatabase : RoomDatabase() {
    abstract fun fruitDao(): FruitDao

    companion object {
        @Volatile
        private var INSTANCE: FruitDatabase? = null

        fun getInstance(context: Context): FruitDatabase {
            synchronized(this) {
                var instance: FruitDatabase? = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        FruitDatabase::class.java,
                        "fruit_database"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}