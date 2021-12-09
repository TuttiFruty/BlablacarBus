package fr.tuttifruty.blablacarbus.ui.fares

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.domain.model.FareDomainModel

class FaresAdapter :
    DataBindingAdapter<FareDomainModel, ViewModel>(DiffCallback(), viewModel = null) {

    class DiffCallback : DiffUtil.ItemCallback<FareDomainModel>() {
        override fun areItemsTheSame(
            oldItem: FareDomainModel,
            newItem: FareDomainModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: FareDomainModel,
            newItem: FareDomainModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.component_fares_dialog_item_line

}
