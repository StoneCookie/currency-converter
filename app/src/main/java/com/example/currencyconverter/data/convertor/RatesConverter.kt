package com.example.currencyconverter.data.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RatesConverter {
    @TypeConverter
    fun fromRate(rates: Map<String, Double>): String{
        return Gson().toJson(rates)
    }

    @TypeConverter
    fun toRate(rates: String): Map<String, Double>{
        val listType = object : TypeToken<Map<String, Double>>() {}.type
        return Gson().fromJson(rates, listType)
    }
}