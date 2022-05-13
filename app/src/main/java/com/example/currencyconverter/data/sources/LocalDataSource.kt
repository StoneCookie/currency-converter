package com.example.currencyconverter.data.sources

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currencyconverter.data.convertor.RatesConverter
import com.example.currencyconverter.data.dao.AppDAO
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.data.model.VaultPair

@Database(entities = [VaultPair::class, History::class, LikedVault::class], version = 5)
@TypeConverters(RatesConverter::class)
abstract class LocalDataSource : RoomDatabase() {

    abstract fun vaultDAO() : AppDAO

    companion object{
        @Volatile
        private var INSTANCE: LocalDataSource? = null

        fun getDatabase(context: Context): LocalDataSource {
            if (INSTANCE == null) {
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        LocalDataSource::class.java,
                        "localDB")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}