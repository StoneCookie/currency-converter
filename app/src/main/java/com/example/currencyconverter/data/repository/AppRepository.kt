package com.example.currencyconverter.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.currencyconverter.data.dao.AppService
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.data.model.Vault
import com.example.currencyconverter.data.model.VaultPair
import com.example.currencyconverter.data.sources.LocalDataSource
import com.example.currencyconverter.utils.NetworkUtils

class AppRepository
    (
    private val appService: AppService,
    private val localDataSource: LocalDataSource,
    private val applicationContext: Context
) {

    private val vaultsLiveData = MutableLiveData<VaultPair>()
    private val historyLiveData = MutableLiveData<List<History>>()
    private val likedVaultsData = MutableLiveData<List<LikedVault>>()

    val history: LiveData<List<History>>
        get() = historyLiveData

    val vaults: LiveData<VaultPair>
        get() = vaultsLiveData

    val likedVaults: LiveData<List<LikedVault>>
        get() = likedVaultsData

    suspend fun getHistory() {
        val history = localDataSource.vaultDAO().getHistory()
        historyLiveData.postValue(history)
    }

    suspend fun setHistory(item: History) {
        localDataSource.vaultDAO().insertHistory(item)
        getHistory()
    }

    suspend fun getLikedVaults(){
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getLatestVault()
            val _array = runCatching { result }
            var likedVaultList: MutableList<LikedVault> = mutableListOf()
            _array.onSuccess {
                if (localDataSource.vaultDAO().getLikedVaults().isEmpty()) {
                    it.rates.forEach {
                        if (it.key != "XDR") {
                            likedVaultList.add(LikedVault(0, it.key, it.value, false))
                        }
                    }
                    localDataSource.vaultDAO().insertLikedVault(likedVaultList)
                }else{
                    it.rates.forEach {
                        if (it.key != "XDR") {
                            localDataSource.vaultDAO().updateLikedVaultsPrice(it.value, it.key)
                        }
                    }
                    likedVaultList = localDataSource.vaultDAO().getLikedVaults() as MutableList<LikedVault>
                }
            }
            likedVaultsData.postValue(likedVaultList)

        } else {
            val vaults = localDataSource.vaultDAO().getLikedVaults()
            likedVaultsData.postValue(vaults)
        }
    }

    suspend fun likeVault(name: String){
        localDataSource.vaultDAO().updateLikedVaultsLike(true, name)
    }

    suspend fun dislikeVault(name: String){
        localDataSource.vaultDAO().updateLikedVaultsLike(false, name)
    }

    suspend fun getVaults() {
        if (NetworkUtils.isInternetAvailable(applicationContext)) {
            val result = appService.getLatestVault()
            localDataSource.vaultDAO().insertVaults(result)
            vaultsLiveData.postValue(result)
        } else {
            val vaults = localDataSource.vaultDAO().getVaults()
            vaultsLiveData.postValue(vaults)
        }
    }
}