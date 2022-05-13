package com.example.currencyconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaults")
data class VaultPair(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val disclaimer: String,
    val date: String,
    val timestamp: Int,
    val base: String,
    val rates: Map<String, Double>
)
