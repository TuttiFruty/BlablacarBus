package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<GetAllBusStopsUseCase> {
        GetAllBusStopsUseCaseImpl(
            busStopsRepository = get(),
        )
    }
}
