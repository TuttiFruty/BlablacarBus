package fr.tuttifruty.blablacarbus.ws.model

import android.location.Location
import com.squareup.moshi.Json
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class BusStopNetworkModel(
    val id: Int,
    @Json(name = "short_name") val shortName: String,
    @Json(name = "short_name_fr") val shortNameFr: String,
    @Json(name = "short_name_de") val shortNameDe: String,
    @Json(name = "short_name_en") val shortNameEn: String,
    @Json(name = "short_name_it") val shortNameIt: String,
    @Json(name = "short_name_nl") val shortNameNl: String,
    @Json(name = "long_name") val longName: String,
    @Json(name = "long_name_fr") val longNameFr: String,
    @Json(name = "long_name_de") val longNameDe: String,
    @Json(name = "long_name_en") val longNameEn: String,
    @Json(name = "long_name_it") val longNameIt: String,
    @Json(name = "long_name_nl") val longNameNl: String,
    @Json(name = "time_zone") val timeZone: String,
    val latitude: String,
    val longitude: String,
    @Json(name = "destinations_ids") val destinationsIDS: List<Int>,
    @Json(name = "is_meta_gare") val isMetaGare: Boolean,
    val address: String?,
    val stops: List<BusStopNetworkModel>?
) {

    fun initFromLatAndLong() : Location{
        val location = Location("")
        location.latitude = getLatitudeAsDouble()
        location.longitude = getLongitudeAsDouble()
        return location
    }

    private fun getLatitudeAsDouble() : Double{
        return try {
            this.latitude.toDouble()
        }catch (e : NumberFormatException){
            0.0
        }
    }

    private fun getLongitudeAsDouble() : Double{
        return try {
            this.longitude.toDouble()
        }catch (e : NumberFormatException){
            0.0
        }
    }
}

fun BusStopNetworkModel.asDomainModel(): BusStopDomainModel {
    return BusStopDomainModel(
        id = this.id,
        shortName = this.shortName,
        longName = this.longName,
        location = initFromLatAndLong(),
        timeZone = this.timeZone,
        destinations = destinationsIDS,
        isMetaGare = this.isMetaGare,
        address = address?:"",
        stops = this.stops?.map { it.id } ?: emptyList()
    )
}