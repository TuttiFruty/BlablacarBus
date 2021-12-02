package fr.tuttifruty.blablacarbus.domain.repository

import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel

interface BusStopsRepository {
    /**
     * Retrieve all Bus Stops from repository
     * If an error occur we send back a null value
     *
     * @return List of BusStopDomainModel or null
     */
    suspend fun getAllBusStops(): List<BusStopDomainModel>?
}
