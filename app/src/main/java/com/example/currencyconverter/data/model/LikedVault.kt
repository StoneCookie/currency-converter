package com.example.currencyconverter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likedVaults")
data class LikedVault(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val price: Double,
    val is_liked: Boolean
)
