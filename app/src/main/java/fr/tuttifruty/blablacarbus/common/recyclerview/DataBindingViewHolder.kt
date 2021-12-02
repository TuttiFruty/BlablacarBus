package fr.tuttifruty.blablacarbus.common.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import fr.tuttifruty.blablacarbus.BR

class DataBindingViewHolder<T, S: ViewModel?>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: T, viewModel: S?) {
        if(viewModel != null) {
            binding.setVariable(BR.viewModel, viewModel)
        }
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}