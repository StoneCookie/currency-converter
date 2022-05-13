package com.example.currencyconverter.data.dao

import com.example.currencyconverter.data.model.VaultPair
import retrofit2.http.GET

interface AppService {
    @GET("./latest.js")
    suspend fun getLatestVault(): VaultPair
}