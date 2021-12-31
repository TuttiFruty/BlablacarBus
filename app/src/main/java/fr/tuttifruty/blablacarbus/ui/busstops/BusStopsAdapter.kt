package fr.tuttifruty.blablacarbus.ui.busstops

import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.lriccardo.timelineview.TimelineAdapter
import com.lriccardo.timelineview.TimelineView
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingAdapter
import fr.tuttifruty.blablacarbus.common.recyclerview.DataBindingViewHolder

class BusStopsAdapter(
    viewModel: BusStopsViewModel?,
    private val onClick: (busStop: BusStopsUIModel) -> Unit
) : DataBindingAdapter<BusStopsUIModel, BusStopsViewModel>(DiffCallback(), viewModel),
    TimelineAdapter {


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

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<BusStopsUIModel, BusStopsViewModel> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return DataBindingViewHolder(binding) {
            onClick(currentList[it])
        }
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<BusStopsUIModel, BusStopsViewModel>,
        position: Int
    ) {
        super.onBindViewHolder(holder, position)
        //https://levelup.gitconnected.com/android-recyclerview-animations-in-kotlin-1e323ffd39be
        //https://howtodoandroid.com/android-recyclerview-item-animations/
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.alpha)
    }

    override fun getItemViewType(position: Int) = R.layout.component_bus_stop_line

    override fun getIndicatorStyle(position: Int): TimelineView.IndicatorStyle? {
        return if (position <= 0)
            TimelineView.IndicatorStyle.Checked
        else TimelineView.IndicatorStyle.Empty
    }

    override fun getLineStyle(position: Int): TimelineView.LineStyle? {
        if (position > 1)
            return TimelineView.LineStyle.Normal
        return super.getLineStyle(position)
    }

}