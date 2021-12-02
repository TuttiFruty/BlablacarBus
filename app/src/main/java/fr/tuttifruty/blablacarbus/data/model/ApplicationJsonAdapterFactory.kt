package fr.tuttifruty.blablacarbus.data.model

import com.squareup.moshi.JsonAdapter
import se.ansman.kotshi.KotshiJsonAdapterFactory

@KotshiJsonAdapterFactory
abstract class ApplicationJsonAdapterFactory : JsonAdapter.Factory {
    @Suppress("unused")
    val instance: ApplicationJsonAdapterFactory = KotshiApplicationJsonAdapterFactory
}