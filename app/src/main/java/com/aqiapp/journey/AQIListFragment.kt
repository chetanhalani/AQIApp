package com.aqiapp.journey

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.aqiapp.R
import com.aqiapp.adapter.AQIListAdapter
import com.aqiapp.base.BaseFragment
import com.aqiapp.databinding.AqiListFragmentBinding
import com.aqiapp.viewmodel.AQIListViewModel
import com.google.android.material.transition.MaterialContainerTransform

class AQIListFragment : BaseFragment(), AQIListFragmentNavigation {

    private val viewModel: AQIListViewModel by activityViewModels()
    private lateinit var binding: AqiListFragmentBinding
    private lateinit var adapter: AQIListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AqiListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.layoutContainer
            duration = 3000L
            scrimColor = Color.TRANSPARENT
        }
        adapter = AQIListAdapter(this)
        binding.rvAQI.adapter = adapter
        val itemDecoration = DividerItemDecoration(binding.rvAQI.context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ColorDrawable(Color.BLACK))
        binding.rvAQI.addItemDecoration(itemDecoration)
        activity?.let { it ->
            viewModel.cityWithAQIList.observe(it) { list ->
                with(adapter){
                    updateData(list)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun navigateToDetail(position: Int) {
        adapter.getItemAt(position)?.let {
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.layoutContainer, AQIDetailFragment(it))?.addToBackStack("")?.commit()
        }
    }
}

interface AQIListFragmentNavigation {
    fun navigateToDetail(position: Int)
}