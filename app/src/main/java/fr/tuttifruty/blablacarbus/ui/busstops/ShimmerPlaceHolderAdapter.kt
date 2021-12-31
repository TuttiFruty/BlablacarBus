package fr.tuttifruty.blablacarbus.ui.busstops

import androidx.recyclerview.widget.DiffUtil
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.common.recyclerview.PlaceHolderAdapter

class ShimmerPlaceHolderAdapter : DataBindingAdapter<BusStopsUIModel, BusStopsViewModel>(
    ShimmerPlaceHolderAdapter.DiffCallback(), null
), PlaceHolderAdapter {

    class DiffCallback : DiffUtil.ItemCallback<BusStopsUIModel>() {
        override fun areItemsTheSame(
            oldItem: BusStopsUIModel,
            newItem: BusStopsUIModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: BusStopsUIModel,
            newItem: BusStopsUIModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    init {
        submitList(List(20) { BusStopsUIModel(it, "Loading name", "Loading Address") })
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.component_shimmer_placeholder_line
    }
}