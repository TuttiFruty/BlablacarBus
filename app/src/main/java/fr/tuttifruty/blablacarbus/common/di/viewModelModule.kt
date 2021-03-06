package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.ui.busstopdetails.BusStopDetailsViewModel
import fr.tuttifruty.blablacarbus.ui.busstops.BusStopsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        BusStopsViewModel(
            getAllBusStopsUseCase = get(),
            persistAllBusStopsUseCase = get()
        )
    }
    viewModel { (currentBusStopId: Int) ->
        BusStopDetailsViewModel(
            getAllBusStopsUseCase = get(),
            getBusStopUseCase = get(),
            getFaresForDestinationUseCase = get(),
            currentBusStopId = currentBusStopId
        )
    }
}