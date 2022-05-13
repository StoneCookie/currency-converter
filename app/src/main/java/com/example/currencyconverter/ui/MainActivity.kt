package com.example.currencyconverter.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.R
import com.example.currencyconverter.data.viewmodel.AppViewModel
import com.example.currencyconverter.data.viewmodel.AppViewModelFactory
import com.example.currencyconverter.databinding.ActivityMainBinding
import com.example.currencyconverter.ui.detail.DetailFragment
import com.example.currencyconverter.ui.filter.FilterFragment
import com.example.currencyconverter.ui.graph.GraphFragment
import com.example.currencyconverter.ui.history.HistoryFragment
import com.example.currencyconverter.ui.vault.VaultFragment

interface HistoryInterface {
    fun openVaultWindow()
    fun openFilterWindow()
    fun closeFilterWindow()
}

interface VaultInterface {
    fun openDetailWindow()
}

interface DetailInterface {
    fun closeDetailWindow()
}

class MainActivity : AppCompatActivity(), HistoryInterface, VaultInterface, DetailInterface {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: AppViewModel

    private val detail = DetailFragment()
    private val vault = VaultFragment()
    private val history = HistoryFragment()
    private val graphic = GraphFragment()
    private val filter = FilterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        val startDate = findViewById<EditText>(R.id.start_date)
        val finishDate = findViewById<EditText>(R.id.finish_date)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.navBar.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.home -> replaceFragment(vault)
                R.id.history -> replaceFragment(history)
                R.id.graphic -> replaceFragment(graphic)
            }

            true
        }

        loadScreen()


        val repository = (application as MainApplication).appRepository

        mainViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository))[AppViewModel::class.java]

        setContentView(binding.root)
    }

    private fun loadScreen() {
        replaceFragment(vault)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        when (fragment) {
            detail -> supportActionBar?.setDisplayHomeAsUpEnabled(true)
            history, filter -> supportActionBar?.title =
                "Фильтр: ${when(FilterFragment.filterTitle) {
                    1 -> "За всё время"
                    2 -> "За неделю"
                    3 -> "За месяц"
                    else -> "Период"
                }}"
            else -> {
                supportActionBar?.title = getString(R.string.app_name)
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        transaction.replace(binding.fragmentContainer.id, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_filter, menu)
        return false

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> openVaultWindow()
            R.id.filter -> openFilterWindow()
        }
        return true
    }

    override fun openVaultWindow() {
        replaceFragment(vault)
    }

    override fun openDetailWindow() {
        replaceFragment(detail)
    }

    override fun closeDetailWindow() {
        replaceFragment(vault)
    }

    override fun closeFilterWindow() {
        replaceFragment(history)
    }

    override fun openFilterWindow() {
        replaceFragment(filter)
    }
}