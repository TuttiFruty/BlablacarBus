package fr.tuttifruty.blablacarbus.ui.busstopdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingViewHolder
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.ui.busstops.BusStopsUIModel
import fr.tuttifruty.blablacarbus.ui.busstops.BusStopsViewModel

class DestinationsAdapter(
    viewModel: BusStopDetailsViewModel?,
    private val onClick: (busStop: BusStopDomainModel) -> Unit
) : DataBindingAdapter<BusStopDomainModel, BusStopDetailsViewModel>(DiffCallback(), viewModel) {

    class DiffCallback : DiffUtil.ItemCallback<BusStopDomainModel>() {
        override fun areItemsTheSame(
            oldItem: BusStopDomainModel,
            newItem: BusStopDomainModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BusStopDomainModel,
            newItem: BusStopDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<BusStopDomainModel, BusStopDetailsViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding){
            onClick(currentList[it])
        }
    }

    override fun getItemViewType(position: Int) = R.layout.component_destination_line
}