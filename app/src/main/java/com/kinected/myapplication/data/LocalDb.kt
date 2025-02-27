package com.kinected.myapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ResultsItem::class], version = 3, exportSchema = false)
abstract class LocalDb: RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object{
        @Volatile
        private var INSTANCE: LocalDb? = null

        fun getInstance(context: Context) : LocalDb {

            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                        context,
                        LocalDb::class.java,
                        "app_database"
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}