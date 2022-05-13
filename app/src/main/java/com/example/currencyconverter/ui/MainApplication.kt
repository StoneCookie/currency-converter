package com.example.currencyconverter.ui

import android.app.Application
import com.example.currencyconverter.data.dao.AppService
import com.example.currencyconverter.data.repository.AppRepository
import com.example.currencyconverter.data.sources.LocalDataSource
import com.example.currencyconverter.data.sources.RemoteDataSource

class MainApplication : Application() {

    lateinit var appRepository: AppRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val quoteService = RemoteDataSource.getInstance().create(AppService::class.java)
        val database = LocalDataSource.getDatabase(applicationContext)
        appRepository = AppRepository(quoteService, database, applicationContext)
    }
}