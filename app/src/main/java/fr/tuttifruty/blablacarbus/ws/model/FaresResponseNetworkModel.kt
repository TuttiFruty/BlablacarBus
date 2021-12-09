package fr.tuttifruty.blablacarbus.ws.model

import se.ansman.kotshi.JsonSerializable

@JsonSerializable
data class FaresResponseNetworkModel(
    val fares: List<FareNetworkModel>,
)
