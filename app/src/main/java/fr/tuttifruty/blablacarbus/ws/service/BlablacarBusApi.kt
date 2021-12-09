package fr.tuttifruty.blablacarbus.ws.service

import fr.tuttifruty.blablacarbus.ws.model.StopsResponseNetworkModel
import retrofit2.Response
import retrofit2.http.GET

interface BlablacarBusApi {
    @GET("stops")
    suspend fun getAllStops(): Response<StopsResponseNetworkModel>
}
