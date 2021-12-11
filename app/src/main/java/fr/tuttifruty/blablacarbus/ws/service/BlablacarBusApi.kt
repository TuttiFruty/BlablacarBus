package fr.tuttifruty.blablacarbus.ws.service

import fr.tuttifruty.blablacarbus.ws.model.BusStopsResponseNetworkModel
import fr.tuttifruty.blablacarbus.ws.model.FaresResponseNetworkModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BlablacarBusApi {
    @GET("stops")
    suspend fun getAllStops(): Response<BusStopsResponseNetworkModel>

    @GET("fares")
    suspend fun getFares(
        @Query("origin_id") originId: Int,
        @Query("destination_id") destinationId: Int,
    ): Response<FaresResponseNetworkModel>
}
