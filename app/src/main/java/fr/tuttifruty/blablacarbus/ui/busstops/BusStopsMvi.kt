package fr.tuttifruty.blablacarbus.ui.busstops

import android.location.Location
import fr.tuttifruty.blablacarbus.common.mvi.IIntent
import fr.tuttifruty.blablacarbus.common.mvi.INavigation
import fr.tuttifruty.blablacarbus.common.mvi.IState

sealed class BusStopsIntent : IIntent {
    class RefreshBusStops(
        val query: String? = null,
        val coordinates: Location? = null
    ) : BusStopsIntent()

    sealed class Navigation : BusStopsIntent() {
        class GoToDetailsOfBusStop(val busStopId: Int) : Navigation()
    }
}

sealed class BusStopsNavigation : INavigation {
    class GoToDetailsOfBusStop(val busStopId: Int) : BusStopsNavigation()
}

sealed class BusStopsState : IState {
    data class ShowBusStops(
        val busStopsList: List<BusStopsUIModel> = emptyList(),
        val isFiltered: Boolean = false,
        val hasSearchInProgress: Boolean = false,
        val lastSearch: String? = null,
        val lastFilter: Location? = null,
    ) : BusStopsState()
    data class ShowError(val message: Int) : BusStopsState()
    object Loading : BusStopsState()
}

data class BusStopsUIModel(
    val id: Int,
    val shortName: String,
    val address: String,
)
