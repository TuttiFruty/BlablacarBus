package fr.tuttifruty.blablacarbus.ui.busstops

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.tuttifruty.blablacarbus.R
import fr.tuttifruty.blablacarbus.common.Event
import fr.tuttifruty.blablacarbus.common.mvi.IModel
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class BusStopsViewModel(
    private val getAllBusStopsUseCase: GetAllBusStopsUseCase
) : ViewModel(), IModel<BusStopsState, BusStopsIntent, BusStopsNavigation> {

    override val intents: Channel<BusStopsIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableLiveData<BusStopsState>().apply { value = BusStopsState.Loading }
    override val state: LiveData<BusStopsState>
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
                    is BusStopsIntent.RefreshBusStops -> fetchData()
                    is BusStopsIntent.Navigation.GoToDetailsOfBusStop -> {
                        _navigation.postValue(Event(BusStopsNavigation.GoToDetailsOfBusStop(busStopsIntent.busStop)))
                    }
                }
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            val result = getAllBusStopsUseCase.invoke()
            updateState {
                if (result.isSuccess) {
                    BusStopsState.ShowBusStops(result.getOrNull()?.busStop ?: emptyList())
                } else {
                    BusStopsState.ShowError(
                        when (result.exceptionOrNull()) {
                            is GetAllBusStopsUseCase.Errors.FailedRetrieveBusStops -> R.string.failed_bus_stops_retrieved
                            else -> R.string.generic_error
                        }
                    )
                }
            }

        }
    }

    private suspend fun updateState(handler: suspend (intent: BusStopsState) -> BusStopsState) {
        _state.postValue(handler(state.value!!))
    }
}