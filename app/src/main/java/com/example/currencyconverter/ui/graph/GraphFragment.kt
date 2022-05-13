package com.example.currencyconverter.ui.graph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.currencyconverter.R
import com.example.currencyconverter.data.model.Vault
import com.example.currencyconverter.data.viewmodel.AppViewModel
import com.example.currencyconverter.data.viewmodel.AppViewModelFactory
import com.example.currencyconverter.databinding.FragmentGraphBinding
import com.example.currencyconverter.ui.MainApplication
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch

class GraphFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var mainViewModel: AppViewModel
    private lateinit var binding: FragmentGraphBinding

    private var graphList: MutableList<Vault> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGraphBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository

        mainViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository))[AppViewModel::class.java]

        barChart = binding.barChart

        buildGraph()

        return binding.root

    }

    private fun initBarChart() {

        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        val yAxis: YAxis = barChart.axisLeft
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        barChart.axisRight.isEnabled = false
        barChart.setDrawValueAboveBar(false)
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setExtraOffsets(15f, 0f, 15f, 10f)
        barChart.animateY(1000)
        barChart.setVisibleXRange(10f, 10f)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 20f
        yAxis.textSize = 20f

        val labels = mutableListOf("")
        for (i in graphList.indices) {
            labels.add(graphList[i].name)
        }

        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f

    }

    private fun buildGraph() {
        lifecycleScope.launch {
            val vaultList: MutableList<Vault> = mutableListOf()
            lifecycleScope.launch {
                mainViewModel.getVaults().observe(viewLifecycleOwner, Observer { it ->
                    if (vaultList.isNotEmpty()) return@Observer
                    it.rates.forEach {
                        if (it.key == "EUR" ||
                            it.key == "USD" ||
                            it.key == "GBP" ||
                            it.key == "CNY"
                        ) {
                            vaultList.add(Vault(it.key, it.value))
                        }
                    }
                    graphList = vaultList
                    barChart.notifyDataSetChanged()
                    barChart.invalidate()

                    initBarChart()

                    val entries = ArrayList<BarEntry>()

                    for (i in graphList.indices) {
                        entries.add(BarEntry((i + 1).toFloat(), graphList[i].price.toFloat()))
                    }

                    val barDataSet = BarDataSet(entries, "")
                    barDataSet.setColors(*ColorTemplate.MATERIAL_COLORS)
                    barDataSet.valueTextSize = 18f
                    barDataSet.valueTextColor = ContextCompat.getColor(requireContext(), R.color.white)

                    val data = BarData(barDataSet)
                    barChart.data = data

                    barChart.invalidate()
                })
            }
        }
    }
}