package fr.tuttifruty.blablacarbus.common.di

import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val androidModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
}