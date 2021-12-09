package fr.tuttifruty.blablacarbus.domain.repository

import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel

interface BusStopsLocalRepository {
    /**
     * Retrieve all Bus Stops from repository
     * If an error occur we send back a null value
     *
     * @return a flow of List of BusStopDomainModel or null
     */
    suspend fun getAllBusStops(query: String?): List<BusStopDomainModel>
    suspend fun getAllBusStopsByListIDs(listsIDs : List<Int>): List<BusStopDomainModel>
    suspend fun persistAllBusStops(busStops: List<BusStopDomainModel>)
    suspend fun getBusStopByID(busStopID: Int): BusStopDomainModel?
    suspend fun countAllBusStops(): Int
}
