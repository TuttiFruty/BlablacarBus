package fr.tuttifruty.blablacarbus.common.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBindingAdapter<T, S: ViewModel?>(diffCallback: DiffUtil.ItemCallback<T>, var viewModel: S?) :
    ListAdapter<T, DataBindingViewHolder<T, S>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T, S> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<T, S>, position: Int) = holder.bind(getItem(position),viewModel)

    abstract override fun getItemViewType(position: Int): Int

}