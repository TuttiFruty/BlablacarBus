package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.data.repository.BusStopsRepositoryImpl
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BusStopsRepository> { BusStopsRepositoryImpl(blablacarBusApi = get()) }
}