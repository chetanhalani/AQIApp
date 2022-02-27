package com.aqiapp.journey

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.aqiapp.R
import com.aqiapp.base.BaseFragment
import com.aqiapp.databinding.AqiDetailFragmentBinding
import com.aqiapp.viewmodel.AQIListViewModel
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class AQIDetailFragment(val cityAQIDetail: AQIListViewModel.CityWithAQIList) : BaseFragment() {

    private val viewModel: AQIListViewModel by activityViewModels()
    private lateinit var binding: AqiDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AqiDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCityName.text = cityAQIDetail.getCityName()
        updateDataInGraphView(cityAQIDetail)
        activity?.let { it ->
            viewModel.cityWithAQIList.observe(it) { list ->
                for (i in list.indices) {
                    if (list[i].getCityName() == cityAQIDetail.getCityName()) {
                        updateDataInGraphView(list[i])
                    }
                }
            }
        }
    }

    private fun updateDataInGraphView(cityWithAQIList: AQIListViewModel.CityWithAQIList) {
        binding.tvLastUpdateAt.text = context?.getString(R.string.updated_at).plus(cityWithAQIList.getLastUpdated())
        binding.tvCurrentAQI.text = cityWithAQIList.getLatestAQI()
        cityWithAQIList.getAQIList()?.let {
            binding.lineChart.data = getDataForChart(it)
            binding.lineChart.postInvalidate()
        }
    }

    private fun getDataForChart(indexes: ArrayList<Double>): LineData {
        val aqIndexList = ArrayList<Entry>()
        for(i in 0 until indexes.size) {
            aqIndexList.add(Entry((i+1).toFloat(), indexes[i].toFloat()))
        }
        return getLineDataSet(aqIndexList)
    }

    private fun getLineDataSet(indexList: ArrayList<Entry>): LineData {
        val set1 = LineDataSet(indexList, "AQIDataSet")
        set1.apply {
            axisDependency = AxisDependency.LEFT
            color = ColorTemplate.getHoloBlue()
            valueTextColor = ColorTemplate.getHoloBlue()
            lineWidth = 1.5f
            setDrawCircles(false)
            setDrawValues(false)
            fillAlpha = 65
            fillColor = ColorTemplate.getHoloBlue()
            highLightColor = Color.rgb(244, 117, 117)
            setDrawCircleHole(false)
        }
        val data = LineData(set1)
        data.apply {
            setValueTextColor(Color.WHITE)
            setValueTextSize(9f)
        }
        return data
    }

}