package fr.tuttifruty.blablacarbus.device.database.model

import android.location.Location
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fr.tuttifruty.blablacarbus.domain.model.BusStopDomainModel
import java.lang.reflect.Type

@Entity(tableName = "bus_stop")
@TypeConverters(BusStopConverter::class)
data class BusStopDatabaseModel(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "short_name")
    val shortName: String,
    @ColumnInfo(name = "long_name")
    val longName: String,
    @ColumnInfo(name = "timezone")
    val timeZone: String,
    @ColumnInfo(name = "location")
    val location: LocationDatabaseModel,
    @ColumnInfo(name = "destinations")
    val destinations: List<Int>,
    @ColumnInfo(name = "is_meta_gare")
    val isMetaGare: Boolean,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "stops")
    val stops: List<Int>
) {
    companion object {
        fun fromDomainModel(busStopDomainModel: BusStopDomainModel): BusStopDatabaseModel {
            return BusStopDatabaseModel(
                busStopDomainModel.id,
                busStopDomainModel.shortName,
                busStopDomainModel.longName,
                busStopDomainModel.timeZone,
                LocationDatabaseModel.fromDomainModel(busStopDomainModel.location),
                busStopDomainModel.destinations,
                busStopDomainModel.isMetaGare,
                busStopDomainModel.address,
                busStopDomainModel.stops
            )
        }
    }
}

fun BusStopDatabaseModel.asDomainModel(): BusStopDomainModel {
    return BusStopDomainModel(
        id,
        shortName,
        longName,
        timeZone,
        location.asDomailModel(),
        destinations,
        isMetaGare,
        address,
        stops,
    )
}

data class LocationDatabaseModel(
    val longitude: Double,
    val latitude: Double
) {
    companion object {
        fun fromDomainModel(location: Location): LocationDatabaseModel {
            return LocationDatabaseModel(
                longitude = location.longitude,
                latitude = location.latitude
            )
        }
    }
}

fun LocationDatabaseModel.asDomailModel(): Location {
    val location = Location("")
    location.longitude = this.longitude
    location.latitude = this.latitude
    return location
}


class BusStopConverter {
    @TypeConverter
    fun storeListIntToString(data: List<Int>): String =
        Gson().toJson(data)

    @TypeConverter
    fun storeStringToIntType(value: String): List<Int> {
        val listType: Type =
            object : TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun storeLocationDatabaseModelToString(data: LocationDatabaseModel): String =
        Gson().toJson(data)

    @TypeConverter
    fun storeStringToLocation(value: String): LocationDatabaseModel {
        val type: Type =
            object : TypeToken<LocationDatabaseModel?>() {}.type
        return Gson().fromJson(value, type)
    }
}
