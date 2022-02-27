package com.aqiapp.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aqiapp.R
import com.aqiapp.databinding.AqiListRowViewBinding
import com.aqiapp.viewmodel.AQIListViewModel

class RowViewHolder private constructor(private val binding: AqiListRowViewBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(cityWithAQIList: AQIListViewModel.CityWithAQIList?) {
        cityWithAQIList?.let {
            binding.aqiData = it
        }
        binding.mContext = binding.root.context
        binding.executePendingBindings()
    }

    fun showStaticView() {
        binding.tvCity.text = binding.tvCity.context.getString(R.string.city)
        binding.tvCurrentAQI.text = binding.tvCity.context.getString(R.string.current_aqi)
        binding.tvLastUpdated.text = binding.tvCity.context.getString(R.string.last_updated)
        binding.tvCurrentAQI.setTextColor(Color.BLACK)
    }

    fun setMainViewOnClick(position: Int, listener: View.OnClickListener) {
        binding.mainRowView.tag = position
        binding.mainRowView.setOnClickListener(listener)
    }

    companion object {
        fun from(parent: ViewGroup): RowViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AqiListRowViewBinding.inflate(layoutInflater, parent, false)
            return RowViewHolder(binding)
        }
    }
}