package fr.tuttifruty.blablacarbus.device.database.repository

import fr.tuttifruty.blablacarbus.device.database.dao.BusStopDao
import fr.tuttifruty.blablacarbus.device.database.model.BusStopDatabaseModel
import fr.tuttifruty.blablacarbus.device.database.model.asDomainModel
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.repository.BusStopsLocalRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BusStopsLocalRepositoryImpl(
    private val dao: BusStopDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BusStopsLocalRepository {
    override suspend fun getAllBusStops(query: String?): List<BusStopDomainModel> {
        return withContext(dispatcher) {
            if (query != null) {
                dao.getAllBusStopByQuery(query).map { it.asDomainModel() }
            } else {
                dao.getAllBusStop().map { it.asDomainModel() }
            }
        }
    }

    override suspend fun getAllBusStopsByListIDs(
        listsIDs: List<Int>,
        query: String?
    ): List<BusStopDomainModel> {
        return withContext(dispatcher) {
            dao.getAllBusStopByIds(listsIDs).map { it.asDomainModel() }.filter {
                if (query != null) {
                    it.longName.contains(query, true) || it.shortName.contains(query, true)
                } else {
                    true
                }
            }
        }
    }

    override suspend fun persistAllBusStops(busStops: List<BusStopDomainModel>) {
        withContext(dispatcher) {
            busStops.forEach {
                dao.insertBusStop(
                    BusStopDatabaseModel.fromDomainModel(it)
                )
            }
        }
    }

    override suspend fun getBusStopByID(busStopID: Int): BusStopDomainModel? {
        return withContext(dispatcher) {
            dao.findById(busStopID)?.asDomainModel()
        }
    }

    override suspend fun countAllBusStops(): Int {
        return withContext(dispatcher) {
            dao.countAllBusStop()
        }
    }
}