package fr.tuttifruty.blablacarbus.ui.busstopdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.Event
import fr.tuttifruty.blablacarbus.common.mvi.IModel
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase
import fr.tuttifruty.blablacarbus.domain.usecase.GetBusStopUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class BusStopDetailsViewModel(
    private val getAllBusStopsUseCase: GetAllBusStopsUseCase,
    private val getBusStopUseCase: GetBusStopUseCase,
) : ViewModel(), IModel<BusStopDetailsState, BusStopDetailsIntent, BusStopsNavigation> {

    override val intents: Channel<BusStopDetailsIntent> = Channel(Channel.UNLIMITED)

    private val _state =
        MutableLiveData<BusStopDetailsState>().apply { value = BusStopDetailsState.Loading }
    override val state: LiveData<BusStopDetailsState>
        get() = _state

    private val _navigation = MutableLiveData<Event<BusStopsNavigation>>()
    override val navigation: LiveData<Event<BusStopsNavigation>>
        get() = _navigation

    init {
        handlerIntent()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { busStopsIntent ->
                when (busStopsIntent) {
                    is BusStopDetailsIntent.ShowFaresForDestination -> {
                        //TODO Call use case to calculte fare and show them
                    }
                    is BusStopDetailsIntent.ShowDetails -> {
                        getBusStopUseCase(GetBusStopUseCase.Input(busStopsIntent.busStopId))
                            .onSuccess {
                                val busStop = it.busStop
                                val busStopDestinationsIDs = it.busStop.destinations
                                var listDestinations = emptyList<BusStopDomainModel>()

                                getAllBusStopsUseCase(GetAllBusStopsUseCase.Input(listIDs = busStopDestinationsIDs))
                                    .onSuccess { output ->
                                        listDestinations = output.busStops
                                    }

                                updateState {
                                    BusStopDetailsState.ShowBusStopDetails(
                                        busStop,
                                        listDestinations
                                    )
                                }
                            }
                            .onFailure {
                                updateState { BusStopDetailsState.ShowError(R.string.generic_error) }
                            }

                    }
                }
            }
        }
    }

    private suspend fun updateState(handler: suspend (intent: BusStopDetailsState) -> BusStopDetailsState) {
        _state.postValue(handler(state.value!!))
    }
}