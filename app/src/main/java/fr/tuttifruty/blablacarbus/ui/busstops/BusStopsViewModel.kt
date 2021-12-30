package fr.tuttifruty.blablacarbus.ui.busstops

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.Event
import fr.tuttifruty.blablacarbus.common.mvi.IModel
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase
import fr.tuttifruty.blablacarbus.domain.usecase.PersistAllBusStopsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class BusStopsViewModel(
    private val getAllBusStopsUseCase: GetAllBusStopsUseCase,
    private val persistAllBusStopsUseCase: PersistAllBusStopsUseCase,
) : ViewModel(), IModel<BusStopsState, BusStopsIntent, BusStopsNavigation> {

    override val intents: Channel<BusStopsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableLiveData<BusStopsState>().apply { value = BusStopsState.Loading }
    override val state: LiveData<BusStopsState>
        get() = _state

    private val _navigation = MutableLiveData<Event<BusStopsNavigation>>()
    override val navigation: LiveData<Event<BusStopsNavigation>>
        get() = _navigation

    private var currentSearch: String? = null

    init {
        handlerIntent()
        viewModelScope.launch {
            val result = persistAllBusStopsUseCase()
            if (result.isSuccess) {
                fetchData()
            } else {
                updateState { BusStopsState.ShowError(R.string.generic_error) }
            }
        }
    }

    private fun handlerIntent() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect { busStopsIntent ->
                when (busStopsIntent) {
                    is BusStopsIntent.RefreshBusStops -> fetchData(
                        busStopsIntent.query,
                        busStopsIntent.coordinates
                    )
                    is BusStopsIntent.Navigation.GoToDetailsOfBusStop -> {
                        _navigation.postValue(
                            Event(
                                BusStopsNavigation.GoToDetailsOfBusStop(
                                    busStopsIntent.busStopId
                                )
                            )
                        )
                    }
                }
            }
        }
    }

    private fun fetchData(query: String? = null, coordinates: Location? = null) {
        viewModelScope.launch {
            updateState { BusStopsState.Loading }
            val result = getAllBusStopsUseCase(
                GetAllBusStopsUseCase.Input(
                    query = query,
                    coordinates = coordinates
                )
            )
            updateState {
                if (result.isSuccess) {
                    BusStopsState.ShowBusStops(
                        busStopsList = result.getOrNull()?.busStops?.map {
                            BusStopsUIModel(
                                it.id,
                                it.shortName,
                                it.address
                            )
                        } ?: emptyList(),
                        isFiltered = coordinates != null,
                        hasSearchInProgress = query != null && currentSearch != query,
                        lastSearch = query,
                        lastFilter = coordinates,
                    )
                } else {
                    BusStopsState.ShowError(
                        when (result.exceptionOrNull()) {
                            is GetAllBusStopsUseCase.Errors.FailedRetrieveBusStops -> R.string.failed_bus_stops_retrieved
                            else -> R.string.generic_error
                        }
                    )
                }
            }
            currentSearch = query
        }

    }

    private suspend fun updateState(handler: suspend (intent: BusStopsState) -> BusStopsState) {
        _state.postValue(handler(state.value!!))
    }

}