package fr.tuttifruty.blablacarbus.ws.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class StopsResponseNetworkModel(
    val stops: List<BusStopNetworkModel>
)