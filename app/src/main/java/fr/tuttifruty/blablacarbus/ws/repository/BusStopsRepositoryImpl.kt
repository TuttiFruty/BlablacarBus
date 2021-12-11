package fr.tuttifruty.blablacarbus.ws.repository

import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.model.FareDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import fr.tuttifruty.blablacarbus.ws.model.FareNetworkModel
import fr.tuttifruty.blablacarbus.ws.model.asDomainModel
import fr.tuttifruty.blablacarbus.ws.service.BlablacarBusApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusStopsRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val blablacarBusApi: BlablacarBusApi,
) : BusStopsRepository {
    override suspend fun getAllBusStops(): List<BusStopDomainModel> {
        return withContext(dispatcher) {
            blablacarBusApi.getAllStops()
                .body()
                ?.stops
                ?.map { it.asDomainModel() } ?: emptyList()
        }
    }

    override suspend fun getAllFares(
        originId: Int,
        destinationId: Int,
        startDate: String
    ): List<FareDomainModel> {
        return withContext(dispatcher) {
            val result = blablacarBusApi.getFares(
                originId = originId,
                destinationId = destinationId,
                //startDate = startDate -> Seems to be the cause of the 500 error
            )
                .body()
                ?.fares

            //During development fares api send back 500 error
            //For demonstration purpose, we will mock the response in those case
            result?.map { it.asDomainModel() }
                ?: listOf(
                    FareNetworkModel(),
                    FareNetworkModel(),
                    FareNetworkModel(),
                    FareNetworkModel(),
                    FareNetworkModel(),
                    FareNetworkModel(),
                ).map { it.asDomainModel() }

        }
    }
}
