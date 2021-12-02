package fr.tuttifruty.blablacarbus.domain.usecase

import fr.tuttifruty.blablacarbus.domain.UseCase
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase.Errors
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase.Output
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetAllBusStopsUseCase : UseCase<Nothing?, Result<Output>> {

    data class Output(
        val busStop: List<BusStopDomainModel>
    ) : UseCase.OutputValues

    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedRetrieveBusStops :
            Errors(message = "Failed Retrieve Bus Stops")
    }

}

class GetAllBusStopsUseCaseImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val busStopsRepository: BusStopsRepository,
) : GetAllBusStopsUseCase {
    override suspend fun invoke(input: Nothing?): Result<Output> {
        return withContext(dispatcher) {
            val busStops = busStopsRepository.getAllBusStops()
            if (busStops != null) {
                Result.success(Output(busStops))
            } else {
                Result.failure(Errors.FailedRetrieveBusStops())
            }
        }
    }

}