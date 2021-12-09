package fr.tuttifruty.blablacarbus.ui.busstops

import androidx.recyclerview.widget.DiffUtil
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingViewHolder

class BusStopsAdapter(
    viewModel: BusStopsViewModel?,
    private val onClick: (busStop: BusStopsUIModel) -> Unit
) : DataBindingAdapter<BusStopsUIModel, BusStopsViewModel>(DiffCallback(), viewModel) {


    class DiffCallback : DiffUtil.ItemCallback<BusStopsUIModel>() {
        override fun areItemsTheSame(
            oldItem: BusStopsUIModel,
            newItem: BusStopsUIModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: BusStopsUIModel,
            newItem: BusStopsUIModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<BusStopsUIModel, BusStopsViewModel>,
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