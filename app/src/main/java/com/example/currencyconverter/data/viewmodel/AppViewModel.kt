package com.example.currencyconverter.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.data.model.VaultPair
import com.example.currencyconverter.data.repository.AppRepository

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    suspend fun getVaults(): LiveData<VaultPair> {
        repository.getVaults()
        return repository.vaults
    }

    suspend fun getHistory(): LiveData<List<History>> {
        repository.getHistory()
        return repository.history
    }

    suspend fun setHistory(item: History) {
        repository.setHistory(item)
    }

    suspend fun getLikedVaults(): LiveData<List<LikedVault>> {
        repository.getLikedVaults()
        return repository.likedVaults
    }

    suspend fun likeVault(name: String){
        repository.likeVault(name)
    }

    suspend fun dislikeVault(name: String){
        repository.dislikeVault(name)
    }
}