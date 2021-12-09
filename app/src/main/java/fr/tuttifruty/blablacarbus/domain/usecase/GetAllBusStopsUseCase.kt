package fr.tuttifruty.blablacarbus.domain.usecase

import android.location.Location
import fr.tuttifruty.blablacarbus.domain.UseCase
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsLocalRepository
import fr.tuttifruty.blablacarbus.domain.usecase.GetAllBusStopsUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetAllBusStopsUseCase : UseCase<Input?, Result<Output>> {

    data class Input(
        val query: String? = null,
        val coordinates: Location? = null,
        val listIDs: List<Int>? = null,
    ) : UseCase.InputValues

    data class Output(
        val busStops: List<BusStopDomainModel>
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
    private val busStopsLocalRepository: BusStopsLocalRepository,
) : GetAllBusStopsUseCase {

    override suspend fun invoke(input: Input?): Result<Output> {
        return withContext(dispatcher) {
            if (input?.listIDs != null) {
                val busStops = busStopsLocalRepository.getAllBusStopsByListIDs(input.listIDs)
                Result.success(Output(busStops))
            } else {
                val busStops = busStopsLocalRepository.getAllBusStops(input?.query)
                Result.success(Output(busStops
                    .filter { busStopDomainModel ->
                        filterForCoordinates(
                            busStopDomainModel,
                            input?.coordinates
                        )
                    })
                )
            }
        }
    }

    private fun filterForCoordinates(
        busStopDomainModel: BusStopDomainModel,
        coordinates: Location?
    ): Boolean {
        return if (coordinates != null) {
            coordinates.distanceTo(busStopDomainModel.location) < 30000
        } else {
            true
        }
    }
}