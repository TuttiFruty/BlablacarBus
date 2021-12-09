package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.ui.busstopdetails.BusStopDetailsViewModel
import fr.tuttifruty.blablacarbus.ui.busstops.BusStopsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { BusStopsViewModel(get(), get()) }
    viewModel { BusStopDetailsViewModel(get(), get()) }
}