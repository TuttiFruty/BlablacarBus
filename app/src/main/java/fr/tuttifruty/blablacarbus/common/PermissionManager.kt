package fr.tuttifruty.blablacarbus.common

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import fr.tuttifruty.blablacarbus.R
import java.lang.ref.WeakReference

/**
 * Custom class to manage permissions
 *
 */
class PermissionManager private constructor(private val fragment: WeakReference<Fragment>) {

    private val requiredPermissions = mutableListOf<Permission>()
    private var explicationMessage: String? = null
    private var callback: (Boolean) -> Unit = {}
    private var detailedCallback: (Map<Permission, Boolean>) -> Unit = {}

    private val permissionCheck = fragment.get()
        ?.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            handleResultAndReset(results)
        }


    companion object {
        /**
         * Helper to instantiate new PermissionManager
         */
        fun from(fragment: Fragment) = PermissionManager(WeakReference(fragment))
    }

    //Builder Pattern
    fun explicationMessage(description: String): PermissionManager {
        explicationMessage = description
        return this
    }

    fun request(vararg permission: Permission): PermissionManager {
        requiredPermissions.addAll(permission)
        return this
    }
    //End Builder Pattern

    /**
     * Magic happen here !
     */
    fun checkPermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        handlePermissionRequest()
    }

    private fun handlePermissionRequest() {
        fragment.get()?.let { fragment ->
            when {
                areAllPermissionsGranted(fragment) -> sendPositiveResult()
                shouldShowPermissionRationale(fragment) -> displayRationale(fragment)
                else -> requestPermissions()
            }
        }
    }

    private fun displayRationale(fragment: Fragment) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle(fragment.getString(R.string.dialog_permission_title))
            .setMessage(
                explicationMessage ?: fragment.getString(R.string.dialog_permission_default_message)
            )
            .setCancelable(false)
            .setPositiveButton(fragment.getString(R.string.dialog_permission_button_positive)) { _, _ ->
                requestPermissions()
            }
            .show()
    }

    private fun sendPositiveResult() {
        handleResultAndReset(getPermissionList().associate { it to true })
    }

    private fun handleResultAndReset(grantResults: Map<String, Boolean>) {
        callback(grantResults.all { it.value })
        detailedCallback(grantResults.mapKeys { Permission.from(it.key) })
        reset()
    }

    private fun reset() {
        requiredPermissions.clear()
        explicationMessage = null
        callback = {}
        detailedCallback = {}
    }

    private fun requestPermissions() {
        permissionCheck?.launch(getPermissionList())
    }

    private fun areAllPermissionsGranted(fragment: Fragment): Boolean {
        return requiredPermissions.all { permission ->
            permission.permissions.all { permissionConstant ->
                ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    permissionConstant
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    private fun shouldShowPermissionRationale(fragment: Fragment): Boolean {
        return requiredPermissions.any { permission ->
            permission.permissions.any { permissionConstant ->
                fragment.shouldShowRequestPermissionRationale(permissionConstant)
            }
        }
    }

    private fun getPermissionList(): Array<String> {
        return requiredPermissions.flatMap { it.permissions.toList() }.toTypedArray()
    }
}