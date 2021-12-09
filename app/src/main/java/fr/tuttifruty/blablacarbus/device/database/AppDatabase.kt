package fr.creditagricole.cats.maprotection.technicien.device.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.tuttifruty.blablacarbus.device.database.dao.BusStopDao
import fr.tuttifruty.blablacarbus.device.database.model.BusStopDatabaseModel

@Database(
    entities = [
        BusStopDatabaseModel::class,
               ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busStopDaoDao(): BusStopDao
}