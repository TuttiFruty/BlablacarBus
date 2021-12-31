package fr.tuttifruty.blablacarbus.common.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

//https://github.com/GuilhE/PlaceHolderRecyclerView
class PlaceHolderRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private lateinit var itemAdapter: Adapter<*>
    lateinit var holdersAdapter: PlaceHolderAdapter
    var holdersItemDecoration: ItemDecoration? = null

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter !is PlaceHolderAdapter) {
            itemAdapter = adapter as Adapter<*>
        }
    }

    fun toggleHoldersAdapter(show: Boolean) {
        if (::holdersAdapter.isInitialized) {
            holdersItemDecoration?.let {
                if (show) addItemDecoration(it) else removeItemDecoration(
                    it
                )
            }
            val temp = if (show) holdersAdapter as Adapter<*> else itemAdapter
            if (temp != adapter) {
                adapter = temp
            }
        }
    }

    fun isHoldersAdapterVisible() = ::holdersAdapter.isInitialized && adapter == holdersAdapter
}

interface PlaceHolderAdapter