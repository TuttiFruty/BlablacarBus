package fr.tuttifruty.blablacarbus.data.repository

import fr.tuttifruty.blablacarbus.data.model.asDomainModel
import fr.tuttifruty.blablacarbus.data.service.BlablacarBusApi
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusStopsRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val blablacarBusApi: BlablacarBusApi,
) : BusStopsRepository {
    override suspend fun getAllBusStops(): List<BusStopDomainModel>? {
        return withContext(dispatcher) {
            val result = blablacarBusApi.getAllStops()
                .body()
                ?.stops

            //TODO Add Database persistence so we don't call API multiple times as specified in the API documentation
            // At most once per day, at least once per month
            result?.map { it.asDomainModel(result) }
        }
    }
}