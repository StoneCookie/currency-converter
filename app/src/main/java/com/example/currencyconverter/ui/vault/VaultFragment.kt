package com.example.currencyconverter.ui.vault

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.model.LikedVault
import com.example.currencyconverter.data.viewmodel.AppViewModel
import com.example.currencyconverter.data.viewmodel.AppViewModelFactory
import com.example.currencyconverter.databinding.FragmentVaultBinding
import com.example.currencyconverter.databinding.FragmentVaultDateBinding
import com.example.currencyconverter.ui.MainApplication
import com.example.currencyconverter.ui.VaultInterface
import com.example.currencyconverter.ui.vault.adapter.VaultAdapter
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

interface VaultAdapterInterface {
    fun openDetailWindow()
    fun likeVault(name: String)
    fun dislikeVault(name: String)
    fun loadDate(bindingMinor: FragmentVaultDateBinding)
    fun loadTimestamp(bindingMinor: FragmentVaultDateBinding)
}

class VaultFragment : Fragment() {

    private lateinit var binding: FragmentVaultBinding
    private lateinit var adapter: VaultAdapter
    private lateinit var mainViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentVaultBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository

        mainViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository))[AppViewModel::class.java]

        val vaultInterface = requireActivity() as VaultInterface

        adapter = VaultAdapter(object : VaultAdapterInterface {
            override fun openDetailWindow() {
                vaultInterface.openDetailWindow()
            }

            override fun likeVault(name: String) {
                lifecycleScope.launch {
                    mainViewModel.likeVault(name)
                    loadVaults()
                }
            }

            override fun dislikeVault(name: String) {
                lifecycleScope.launch {
                    mainViewModel.dislikeVault(name)
                    loadVaults()
                }
            }

            override fun loadDate(bindingMinor: FragmentVaultDateBinding) {
                lifecycleScope.launch {
                    mainViewModel.getVaults().observe(viewLifecycleOwner) {
                        bindingMinor.currentDate.text = it.date
                    }
                }
            }

            @SuppressLint("NewApi")
            override fun loadTimestamp(bindingMinor: FragmentVaultDateBinding) {
                lifecycleScope.launch {
                    mainViewModel.getVaults().observe(viewLifecycleOwner) {
                        bindingMinor.currentTime.text = DateTimeFormatter.ofPattern("HH:mm:ss")
                            .withZone(ZoneOffset.of("+03:00"))
                            .format(Instant.now())
                    }
                }
            }
        })

        loadVaults()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.apply {
            vaultRecycler.layoutManager = layoutManager
            vaultRecycler.adapter = adapter
        }

        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadVaults() {
        lifecycleScope.launch {
            mainViewModel.getLikedVaults().observe(viewLifecycleOwner) {
                adapter.vaultList = it as MutableList<LikedVault>
                adapter.notifyDataSetChanged()
            }
        }
    }
}