package fr.tuttifruty.blablacarbus.ws.model

import android.os.Build
import com.squareup.moshi.Json
import fr.tuttifruty.blablacarbus.domain.model.FareDomainModel
import se.ansman.kotshi.JsonSerializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


@JsonSerializable
data class FareNetworkModel(
    val id: Int,
    @Json(name = "udpated_at") val udpatedAt: String?,
    @Json(name = "origin_id") val originId: Int,
    @Json(name = "destination_id") val destinationId: Int,
    val departure: String,
    val arrival: String,
    @Json(name = "price_cents") val priceCents: Int,
    @Json(name = "price_currency") val priceCurrency: String,
    val legs: List<LegNetworkModel>,
) {

    constructor() : this(
        1,
        "2015-05-08T16:45:00.715Z",
        3,
        2,
        "2015-05-12T16:45:00.000+02:00",
        "2015-05-12T16:45:00.000+02:00",
        2900,
        "EUR",
        listOf(
            LegNetworkModel(),
            LegNetworkModel()
        ),
    )

    private val apiDatePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"

    fun getDepartureFormatted(domainDatePattern: String): String {
        return formatDate(this.departure, domainDatePattern)
    }

    fun getArrivalFormatted(domainDatePattern: String): String {
        return formatDate(this.arrival, domainDatePattern)
    }

    fun getPriceCentsAsUnit(): Int {
        return this.priceCents / 100
    }

    private fun formatDate(dateToFormat: String, domainDatePattern: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val inputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern(apiDatePattern, Locale.getDefault())
            val outputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern(domainDatePattern, Locale.getDefault())
            val date: LocalDateTime = LocalDateTime.parse(dateToFormat, inputFormatter)
            outputFormatter.format(date)
        } else {
            val inputFormat = SimpleDateFormat(apiDatePattern, Locale.getDefault())
            val outputFormat = SimpleDateFormat(domainDatePattern, Locale.getDefault())
            val date: Date? = inputFormat.parse(dateToFormat)
            if (date != null) outputFormat.format(this) else ""
        }
    }
}


fun FareNetworkModel.asDomainModel(): FareDomainModel {
    val domainDatePattern = "dd-MM-yyyy HH:mm"
    return FareDomainModel(
        id = this.id,
        departure = getDepartureFormatted(domainDatePattern),
        arrival = getArrivalFormatted(domainDatePattern),
        cost = getPriceCentsAsUnit(),
        currency = priceCurrency,
        firstBusNumber = this.legs.firstOrNull()?.busNumber ?: "",
    )
}