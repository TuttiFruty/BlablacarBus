package fr.tuttifruty.blablacarbus.domain.usecase

import fr.tuttifruty.blablacarbus.domain.UseCase
import fr.tuttifruty.blablacarbus.domain.model.FaresDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import fr.tuttifruty.blablacarbus.domain.usecase.GetFaresForDestinationUseCase.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

interface GetFaresForDestinationUseCase : UseCase<Input, Result<Output>> {

    data class Input(
        val originId: Int,
        val destinationId: Int,
    ) : UseCase.InputValues

    data class Output(
        val fares: FaresDomainModel
    ) : UseCase.OutputValues

    sealed class Errors(
        message: String
    ) : Throwable(message = message) {
        class FailedRetrieveFares :
            Errors(message = "Failed Retrieve Fares")

        class NoFares :
            Errors(message = "No Fares to display")
    }

}

class GetFaresForDestinationUseCaseImpl(
    private val busStopsRepository: BusStopsRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : GetFaresForDestinationUseCase {
    override suspend fun invoke(input: Input?): Result<Output> {
        return withContext(dispatcher) {
            if (input != null) {
                val dateFormatter = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
                val currentDate = dateFormatter.format(Calendar.getInstance().time)

                val result = busStopsRepository.getAllFares(
                    originId = input.originId,
                    destinationId = input.destinationId,
                    startDate = currentDate,
                )
                if (result.isNotEmpty()) {
                    Result.success(Output(FaresDomainModel(result)))
                } else {
                    Result.failure(Errors.NoFares())
                }
            } else {
                Result.failure(Errors.FailedRetrieveFares())
            }
        }
    }

}


