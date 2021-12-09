package fr.tuttifruty.blablacarbus.domain.repository

import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import fr.tuttifruty.blablacarbus.domain.model.FareDomainModel

interface BusStopsRepository {
    /**
     * Retrieve all Bus Stops from repository
     * If an error occur we send back a null value
     *
     * @return List of BusStopDomainModel or null
     */
    suspend fun getAllBusStops(): List<BusStopDomainModel>
    suspend fun getAllFares(
        originId: Int,
        destinationId: Int,
        startDate: String
    ): List<FareDomainModel>
}
