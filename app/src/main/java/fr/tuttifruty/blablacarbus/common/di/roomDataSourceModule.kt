package fr.tuttifruty.blablacarbus.common.di

import androidx.room.Room
import fr.creditagricole.cats.maprotection.technicien.device.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {
    // Room Database
    single {
        Room.databaseBuilder(androidApplication(), AppDatabase::class.java, "bus_stops_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    // Expose PokemonDAO
    single { get<AppDatabase>().busStopDaoDao() }
}