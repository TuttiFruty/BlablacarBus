package fr.tuttifruty.blablacarbus.data.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class StopsResponseNetworkModel(
    val stops: List<BusStopNetworkModel>
)