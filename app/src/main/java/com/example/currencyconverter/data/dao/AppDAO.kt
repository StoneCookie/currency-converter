package com.example.currencyconverter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.data.model.VaultPair

@Dao
interface AppDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaults(vaultPair: VaultPair)

    @Query("SELECT * FROM vaults")
    suspend fun getVaults() : VaultPair

    @Query("DELETE FROM vaults")
    fun nukeTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(historyElement: History)

    @Query("SELECT * FROM history")
    suspend fun getHistory() : List<History>

    @Insert()
    suspend fun insertLikedVault(likedVaultElement: MutableList<LikedVault>)

    @Query("SELECT * FROM likedVaults ORDER BY is_liked DESC")
    suspend fun getLikedVaults() : List<LikedVault>

    @Query("UPDATE likedVaults SET is_liked = :is_liked WHERE name = :name")
    suspend fun updateLikedVaultsLike(is_liked: Boolean, name: String)

    @Query("UPDATE likedVaults SET price = :price WHERE name = :name")
    suspend fun updateLikedVaultsPrice(price: Double, name: String)


}