package com.aqiapp.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aqiapp.journey.AQIListFragmentNavigation
import com.aqiapp.viewmodel.AQIListViewModel

class AQIListAdapter(private val aqiNavigationHandler: AQIListFragmentNavigation): RecyclerView.Adapter<RowViewHolder>() {

    private lateinit var cityWithAQIList: List<AQIListViewModel.CityWithAQIList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        return RowViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return (if(::cityWithAQIList.isInitialized) cityWithAQIList.count() else 0) + 1
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        if(position == 0) {
            holder.showStaticView()
        } else {
            holder.bind(getItemAt(position))
        }
        holder.setMainViewOnClick(position) { v ->
            v?.let {
                val tagPosition = v.tag.toString().toInt()
                aqiNavigationHandler.navigateToDetail(tagPosition)
            }
        }
    }

    fun updateData(list : List<AQIListViewModel.CityWithAQIList>) {
        cityWithAQIList = list
    }

    fun getItemAt(position: Int): AQIListViewModel.CityWithAQIList? {
        return if(position > 0){
            cityWithAQIList[position-1]
        } else {
            null
        }
    }

}
