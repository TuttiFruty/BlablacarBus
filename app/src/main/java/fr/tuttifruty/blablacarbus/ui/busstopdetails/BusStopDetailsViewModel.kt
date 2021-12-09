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
import fr.tuttifruty.blablacarbus.domain.usecase.GetFaresForDestinationUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class BusStopDetailsViewModel(
    private val getAllBusStopsUseCase: GetAllBusStopsUseCase,
    private val getBusStopUseCase: GetBusStopUseCase,
    private val getFaresForDestinationUseCase: GetFaresForDestinationUseCase,
    private val currentBusStopId: Int
) : ViewModel(), IModel<BusStopDetailsState, BusStopDetailsIntent, BusStopDetailsNavigation> {

    override val intents: Channel<BusStopDetailsIntent> = Channel(Channel.UNLIMITED)

    private val _state =
        MutableLiveData<BusStopDetailsState>().apply { value = BusStopDetailsState.Loading }
    override val state: LiveData<BusStopDetailsState>
        get() = _state

    private val _navigation = MutableLiveData<Event<BusStopDetailsNavigation>>()
    override val navigation: LiveData<Event<BusStopDetailsNavigation>>
        get() = _navigation

    init {
        viewModelScope.launch {
            fetchData()
        }
        handlerIntent()
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { busStopsIntent ->
                when (busStopsIntent) {
                    is BusStopDetailsIntent.ShowFaresForDestination -> {
                        handleShowFaresForDestination(busStopsIntent)
                    }
                }
            }
        }
    }

    private suspend fun fetchData() {
        getBusStopUseCase(GetBusStopUseCase.Input(currentBusStopId))
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

    private suspend fun handleShowFaresForDestination(busStopsIntent: BusStopDetailsIntent.ShowFaresForDestination) {
        updateState { BusStopDetailsState.Loading }
        getFaresForDestinationUseCase(
            GetFaresForDestinationUseCase.Input(
                currentBusStopId,
                busStopsIntent.destination.id
            )
        )
            .onSuccess { output ->
                _navigation.postValue(
                    Event(
                        BusStopDetailsNavigation.GoToFaresForDestination(
                            output.fares.listOfFare
                        )
                    )
                )
            }
            .onFailure {
                when (it) {
                    is GetFaresForDestinationUseCase.Errors.NoFares -> updateState {
                        BusStopDetailsState.ShowError(
                            R.string.no_fares
                        )
                    }
                    else -> updateState {
                        BusStopDetailsState.ShowError(
                            R.string.generic_error
                        )
                    }

                }
            }
    }

    private suspend fun updateState(handler: suspend (intent: BusStopDetailsState) -> BusStopDetailsState) {
        _state.postValue(handler(state.value!!))
    }
}