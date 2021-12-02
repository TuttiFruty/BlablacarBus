package fr.tuttifruty.blablacarbus.ui.busstops

import androidx.recyclerview.widget.DiffUtil
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingViewHolder
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel

class BusStopsAdapter(
    viewModel: BusStopsViewModel?,
    private val onClick: (busStop: BusStopDomainModel) -> Unit
) : DataBindingAdapter<BusStopDomainModel, BusStopsViewModel>(DiffCallback(), viewModel) {

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

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<BusStopDomainModel, BusStopsViewModel>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            val busStop = currentList[holder.adapterPosition]
            onClick(busStop)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.component_bus_stop_line
}