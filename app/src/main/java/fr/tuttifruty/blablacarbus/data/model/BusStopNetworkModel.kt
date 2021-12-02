package fr.tuttifruty.blablacarbus.data.model

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
    /**
     * Transform list of destination IDs (Int) into BusStopDomainModel
     */
    fun initDestinationFromBusStopList(busStops: List<BusStopNetworkModel>?): List<BusStopDomainModel> {
        return if (busStops != null) {
            this.destinationsIDS.mapNotNull { destinationsID ->
                busStops.find { destinationsID == it.id }?.asDomainModel()
            }
        } else {
            emptyList()
        }
    }
}

fun BusStopNetworkModel.asDomainModel(busStops: List<BusStopNetworkModel>? = null): BusStopDomainModel {
    return BusStopDomainModel(
        id = this.id,
        shortName = this.shortName,
        longName = this.longName,
        latitude = this.latitude,
        longitude = this.longitude,
        timeZone = this.timeZone,
        destinations = initDestinationFromBusStopList(busStops),
        isMetaGare = this.isMetaGare,
        address = address?:"",
        stops = this.stops?.map { it.asDomainModel() } ?: emptyList()
    )
}