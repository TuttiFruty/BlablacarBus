package fr.tuttifruty.blablacarbus.ws.repository

import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import fr.tuttifruty.blablacarbus.ws.model.asDomainModel
import fr.tuttifruty.blablacarbus.ws.service.BlablacarBusApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusStopsRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val blablacarBusApi: BlablacarBusApi,
) : BusStopsRepository {
    override suspend fun getAllBusStops(): List<BusStopDomainModel>? {
        return withContext(dispatcher) {
            blablacarBusApi.getAllStops()
                .body()
                ?.stops
                ?.map { it.asDomainModel() }
        }
    }
}