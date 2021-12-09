package fr.tuttifruty.blablacarbus.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class FaresDomainModel(
    val listOfFare: List<FareDomainModel>
)

@Parcelize
data class FareDomainModel(
    val id: Int,
    val departure: String,
    val arrival: String,
    val cost: Int,
    val currency: String,
    val firstBusNumber: String,
) : Parcelable
