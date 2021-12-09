package fr.tuttifruty.blablacarbus.device.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.tuttifruty.blablacarbus.device.database.model.BusStopDatabaseModel

@Dao
interface BusStopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBusStop(busStopDatabaseModel: BusStopDatabaseModel): Long

    @Query("SELECT * FROM bus_stop WHERE id LIKE :id LIMIT 1")
    suspend fun findById(id: Int): BusStopDatabaseModel?

    @Query("SELECT * FROM bus_stop ORDER BY id ASC")
    suspend fun getAllBusStop(): List<BusStopDatabaseModel>

    @Query("SELECT * FROM bus_stop WHERE id IN (:listsIDs)")
    suspend fun getAllBusStopByIds(listsIDs: List<Int>): List<BusStopDatabaseModel>

    @Query("SELECT * FROM bus_stop WHERE long_name LIKE '%' || :query || '%' OR short_name LIKE '%' || :query || '%' ORDER BY id ASC")
    suspend fun getAllBusStopByQuery(query: String?): List<BusStopDatabaseModel>

    @Query("SELECT COUNT(*) FROM bus_stop")
    suspend fun countAllBusStop(): Int

}