package fr.tuttifruty.blablacarbus.ui.busstopdetails

import fr.tuttifruty.blablacarbus.common.mvi.IIntent
import fr.tuttifruty.blablacarbus.common.mvi.INavigation
import fr.tuttifruty.blablacarbus.common.mvi.IState
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel

sealed class BusStopDetailsIntent : IIntent {
    class ShowDetails(val busStop: BusStopDomainModel) : BusStopDetailsIntent()
    class ShowFaresForDestination(val destination: BusStopDomainModel) : BusStopDetailsIntent()
}

sealed class BusStopsNavigation : INavigation

sealed class BusStopDetailsState : IState {
    data class ShowBusStopDetails(val busStop : BusStopDomainModel) : BusStopDetailsState()
    data class ShowError(val message: Int) : BusStopDetailsState()
    object Loading : BusStopDetailsState()
}
