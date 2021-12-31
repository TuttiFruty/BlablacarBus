package fr.tuttifruty.blablacarbus.common.recyclerview

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import fr.tuttifruty.blablacarbus.BR

class DataBindingViewHolder<T, S: ViewModel?>(private val binding: ViewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {

    //https://hamurcuabi.medium.com/recyclerview-item-click-in-a-better-way-c69d9c074ddf
    constructor(
        binding: ViewDataBinding,
        onViewHolderClicked: (Int) -> Unit
    ) : this(binding) {
        itemView.setOnClickListener {
            onViewHolderClicked(adapterPosition)
        }
    }

    fun bind(item: T, viewModel: S?) {
        if(viewModel != null) {
            binding.setVariable(BR.viewModel, viewModel)
        }
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}