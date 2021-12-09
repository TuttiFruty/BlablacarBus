package fr.tuttifruty.blablacarbus.common

import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Custom QueryTextListener that leverage Coroutine so we can delay the query typed by the user
 * On new character typed by the user we cancel previous query
 * Delay : 500ms
 * The listener is lifecycle aware no need to handle it, just pass a Lifecycle from Activity or Fragment
 */
class DelayedQueryTextListener(
    lifecycle: Lifecycle,
    private val onDelayedQueryTextChange: (String?) -> Unit,
    private val onDefaultQueryTextSubmit: (String?) -> Unit,
) : SearchView.OnQueryTextListener {
    private val delayedPeriod: Long = 500

    private val coroutineScope = lifecycle.coroutineScope

    private var queryJob: Job? = null

    override fun onQueryTextSubmit(query: String?): Boolean {
        onDefaultQueryTextSubmit(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        queryJob?.cancel()
        queryJob = coroutineScope.launch {
            newText?.let {
                delay(delayedPeriod)
                onDelayedQueryTextChange(newText)
            }
        }
        return false
    }
}