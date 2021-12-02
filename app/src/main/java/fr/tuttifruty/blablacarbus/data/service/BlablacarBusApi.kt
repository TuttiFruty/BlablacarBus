package fr.tuttifruty.blablacarbus.data.service

import fr.tuttifruty.blablacarbus.data.model.StopsResponseNetworkModel
import retrofit2.Response
import retrofit2.http.GET

interface BlablacarBusApi {
    @GET("stops")
    suspend fun getAllStops(): Response<StopsResponseNetworkModel>
}
