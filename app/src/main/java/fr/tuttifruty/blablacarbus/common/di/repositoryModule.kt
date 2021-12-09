package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.device.database.repository.BusStopsLocalRepositoryImpl
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsLocalRepository
import fr.tuttifruty.blablacarbus.ws.repository.BusStopsRepositoryImpl
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BusStopsRepository> { BusStopsRepositoryImpl(blablacarBusApi = get()) }
    single<BusStopsLocalRepository> { BusStopsLocalRepositoryImpl(dao = get()) }
}