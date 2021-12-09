package fr.tuttifruty.blablacarbus.common.di

import fr.tuttifruty.blablacarbus.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    single<GetAllBusStopsUseCase> {
        GetAllBusStopsUseCaseImpl(
            busStopsLocalRepository = get(),
        )
    }
    single<PersistAllBusStopsUseCase> {
        PersistAllBusStopsUseCaseImpl(
            busStopsLocalRepository = get(),
            busStopsRepository = get(),
        )
    }

    single<GetBusStopUseCase> {
        GetBusStopUseCaseImpl(
            busStopsLocalRepository = get(),
        )
    }
}
