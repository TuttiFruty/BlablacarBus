package fr.tuttifruty.blablacarbus.domain.model

import java.io.Serializable
data class BusStopDomainModel (
    val id: Int,
    val shortName: String,
    val longName: String,
    val timeZone: String,
    val latitude: String,
    val longitude: String,
    val destinations: List<BusStopDomainModel>,
    val isMetaGare: Boolean,
    val address: String,
    val stops: List<BusStopDomainModel>
): Serializable
