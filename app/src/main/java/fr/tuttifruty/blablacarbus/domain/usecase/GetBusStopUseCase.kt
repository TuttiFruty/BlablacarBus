package fr.tuttifruty.blablacarbus.domain.usecase

import fr.tuttifruty.blablacarbus.domain.UseCase
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsLocalRepository
import fr.tuttifruty.blablacarbus.domain.usecase.GetBusStopUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GetBusStopUseCase : UseCase<Input, Result<Output>> {

    data class Input(
        val id: Int,
    ) : UseCase.InputValues

    data class Output(
        val busStop: BusStopDomainModel
    ) : UseCase.OutputValues

    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedRetrieveBusStop :
            Errors(message = "Failed Retrieve Bus Stop")
    }

}

class GetBusStopUseCaseImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val busStopsLocalRepository: BusStopsLocalRepository,
) : GetBusStopUseCase {

    override suspend fun invoke(input: Input?): Result<Output> {
        return withContext(dispatcher) {
            if (input?.id != null) {
                val result = busStopsLocalRepository.getBusStopByID(input.id)
                if (result != null) {
                    Result.success(Output(result))
                } else {
                    Result.failure(Errors.FailedRetrieveBusStop())
                }
            } else {
                Result.failure(Errors.FailedRetrieveBusStop())
            }
        }
    }
}