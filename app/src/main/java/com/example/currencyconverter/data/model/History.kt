package com.example.currencyconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class History(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val first_vault: String,
    val sec_vault: String,
    val first_price: String,
    val sec_price: String,
    val date: String
)
