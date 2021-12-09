package fr.tuttifruty.blablacarbus.domain.usecase

import fr.tuttifruty.blablacarbus.domain.UseCase
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsLocalRepository
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PersistAllBusStopsUseCase : UseCase<Nothing?, Result<Nothing?>> {
    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedPersistAllBusStops :
            Errors(message = "Failed persisting all Bus Stops")
    }

}

class PersistAllBusStopsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val busStopsLocalRepository: BusStopsLocalRepository,
    private val busStopsRepository: BusStopsRepository,
) : PersistAllBusStopsUseCase {

    override suspend fun invoke(input: Nothing?): Result<Nothing?> {
        return withContext(dispatcher) {
            val busStops = busStopsRepository.getAllBusStops()
            if (busStops != null) {
                busStopsLocalRepository.persistAllBusStops(busStops)
                Result.success(null)
            } else {
                Result.failure(PersistAllBusStopsUseCase.Errors.FailedPersistAllBusStops())
            }
        }
    }
}