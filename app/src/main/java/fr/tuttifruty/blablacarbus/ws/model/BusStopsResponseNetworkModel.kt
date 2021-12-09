package fr.tuttifruty.blablacarbus.ws.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class BusStopsResponseNetworkModel(
    val stops: List<BusStopNetworkModel>,
)