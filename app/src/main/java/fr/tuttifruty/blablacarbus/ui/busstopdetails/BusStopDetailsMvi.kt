package fr.tuttifruty.blablacarbus.ui.busstopdetails

import fr.tuttifruty.blablacarbus.common.mvi.IIntent
import fr.tuttifruty.blablacarbus.common.mvi.INavigation
import fr.tuttifruty.blablacarbus.common.mvi.IState
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.model.FareDomainModel

sealed class BusStopDetailsIntent : IIntent {
    class ShowFaresForDestination(val destination: BusStopDomainModel) : BusStopDetailsIntent()
}

sealed class BusStopDetailsNavigation : INavigation {
    class GoToFaresForDestination(val fares: List<FareDomainModel>) : BusStopDetailsNavigation()
}

sealed class BusStopDetailsState : IState {
    data class ShowBusStopDetails(
        val busStop: BusStopDomainModel,
        val listDestinations: List<BusStopDomainModel>
    ) : BusStopDetailsState()

    data class ShowError(val message: Int) : BusStopDetailsState()
    object Loading : BusStopDetailsState()
}