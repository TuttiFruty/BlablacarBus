package fr.tuttifruty.blablacarbus.ws.model

import com.squareup.moshi.Json
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class LegNetworkModel(
    @Json(name = "origin_id") val originID: Long,
    @Json(name = "destination_id") val destinationID: Long,
    val departure: String,
    val arrival: String,
    @Json(name = "bus_number") val busNumber: String,
) {
    constructor() : this(
        2,
        3,
        "2019-05-12T16:45:00.000+02:00",
        "2019-05-12T16:45:00.000+02:00",
        "30"
    )
}
