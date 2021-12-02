package fr.tuttifruty.blablacarbus.ui.busstops

import fr.tuttifruty.blablacarbus.common.mvi.IIntent
import fr.tuttifruty.blablacarbus.common.mvi.INavigation
import fr.tuttifruty.blablacarbus.common.mvi.IState
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel

sealed class BusStopsIntent : IIntent {
    object RefreshBusStops : BusStopsIntent()
    sealed class Navigation : BusStopsIntent() {
        class GoToDetailsOfBusStop(val busStop: BusStopDomainModel) : Navigation()
    }
}

sealed class BusStopsNavigation : INavigation {
    class GoToDetailsOfBusStop(val busStop: BusStopDomainModel) : BusStopsNavigation()
}

sealed class BusStopsState : IState {
    data class ShowBusStops(val busStopsList: List<BusStopDomainModel>) : BusStopsState()
    data class ShowError(val message: Int) : BusStopsState()
    object Loading : BusStopsState()
}
