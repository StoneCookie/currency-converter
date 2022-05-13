package com.example.currencyconverter.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.data.viewmodel.AppViewModel
import com.example.currencyconverter.data.viewmodel.AppViewModelFactory
import com.example.currencyconverter.databinding.FragmentDetailBinding
import com.example.currencyconverter.ui.DetailInterface
import com.example.currencyconverter.ui.MainApplication
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private lateinit var mainViewModel: AppViewModel
    private val df = DecimalFormat("#.####")

    override fun onResume() {
        super.onResume()
        binding.defaultVaultValue.setText("1")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentDetailBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository

        mainViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository))[AppViewModel::class.java]

        exchangeButtonClickEvent()

        binding.apply {
            selectedVaultIco.text = getCurrentIco()
            selectedVaultName.text = getVault()
            selectedVaultValue.setText(df.format(getRate()))
            defaultVaultValue.addTextChangedListener(textWatcherDefaultCurrency)
            selectedVaultValue.addTextChangedListener(textWatcherSelectedCurrency)
        }

        return binding.root
    }

    val textWatcherDefaultCurrency: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.selectedVaultValue.removeTextChangedListener(textWatcherSelectedCurrency)
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val resultSelectedVault =
                if (s!!.isEmpty()) "0"
                else df.format(s.toString().toDouble() * getRate())

            binding.exchangeButton.isEnabled = s.toString().isNotEmpty()
            binding.selectedVaultValue.setText(resultSelectedVault)
        }

        override fun afterTextChanged(s: Editable?) {
            binding.selectedVaultValue.addTextChangedListener(textWatcherSelectedCurrency)
        }
    }

    val textWatcherSelectedCurrency: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            binding.defaultVaultValue.removeTextChangedListener(textWatcherDefaultCurrency)
        }

        override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            val resultDefaultVault =
                if (s!!.isEmpty()) "0"
                else df.format((s.toString().toDouble()) / getRate())

            binding.exchangeButton.isEnabled = s.toString().isNotEmpty()
            binding.defaultVaultValue.setText(resultDefaultVault)
        }

        override fun afterTextChanged(s: Editable?) {
            binding.defaultVaultValue.addTextChangedListener(textWatcherDefaultCurrency)
        }
    }

    @SuppressLint("NewApi")
    private fun exchangeButtonClickEvent() {
        binding.exchangeButton.setOnClickListener {
            val item = History(
                0,
                binding.defaultVaultName.text.toString(),
                getVault(),
                binding.defaultVaultValue.text.toString(),
                binding.selectedVaultValue.text.toString(),
                DateTimeFormatter
                    .ofPattern("yyyy-MM-dd")
                    .withZone(ZoneOffset.UTC)
                    .format(Instant.now())
            )
            lifecycleScope.launch {
                mainViewModel.setHistory(item)
                val detailInterface = requireActivity() as DetailInterface
                detailInterface.closeDetailWindow()
            }
        }
    }

    companion object {
        private val valueIco = mapOf(
            "AUD" to "¤",
            "AZN" to "₼",
            "GBP" to "£",
            "AMD" to "֏",
            "BYN" to "Br",
            "BGN" to "¤",
            "BRL" to "R$",
            "HUF" to "Ft",
            "HKD" to "HK$",
            "DKK" to "kr",
            "USD" to "$",
            "EUR" to "€",
            "INR" to "₹",
            "KZT" to "₸",
            "CAD" to "C$",
            "KGS" to "с",
            "CNY" to "¥",
            "MDL" to "L",
            "NOK" to "¤",
            "PLN" to "Zł",
            "RON" to "¤",
            "SGD" to "S$",
            "TJS" to "¤",
            "TRY" to "₺",
            "TMT" to "m",
            "UZS" to "¤",
            "UAH" to "₴",
            "CZK" to "Kč",
            "SEK" to "¤",
            "CHF" to "₣",
            "ZAR" to "¤",
            "KRW" to "₩",
            "JPY" to "¥"
        )
        private lateinit var currentVault: String
        private var currentRate: Double = 0.0
        private lateinit var currentIco: String

        fun getCurrentIco(): String {
            return currentIco
        }

        fun setCurrentIco(name: String) {
            currentIco = name
        }

        fun getIco(): Map<String, String> {
            return valueIco
        }

        fun getVault(): String {
            return currentVault
        }

        fun setVault(name: String) {
            currentVault = name
        }

        fun getRate(): Double {
            return currentRate
        }

        fun setRate(rate: Double) {
            currentRate = rate
        }
    }
}