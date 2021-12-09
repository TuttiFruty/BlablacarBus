package fr.tuttifruty.blablacarbus.domain.model

import android.location.Location
import java.io.Serializable
data class BusStopDomainModel(
    val id: Int,
    val shortName: String,
    val longName: String,
    val timeZone: String,
    val location : Location,
    val destinations: List<Int>,
    val isMetaGare: Boolean,
    val address: String,
    val stops: List<Int>
): Serializable
