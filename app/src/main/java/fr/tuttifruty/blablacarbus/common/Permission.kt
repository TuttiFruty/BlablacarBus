package fr.tuttifruty.blablacarbus.common

import android.Manifest.permission.ACCESS_COARSE_LOCATION

/**
 * Add here all permissions that are mandatory and need approval by the user for specific feature
 * Should be asked on feature launch
 */
sealed class Permission(vararg val permissions: String) {

    // Bundled permissions for Location
    object Location : Permission(ACCESS_COARSE_LOCATION)

    companion object {
        fun from(permission: String) = when (permission) {
            ACCESS_COARSE_LOCATION -> Location
            else -> throw IllegalArgumentException("Unknown permission: $permission")
        }
    }
}