package fr.tuttifruty.blablacarbus.common

import android.text.Editable
import android.text.TextWatcher
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
class DelayedTextWatcher(
    lifecycle: Lifecycle,
    private val onDefaultBeforeTextChanged: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit = { _, _, _, _ -> },
    private val onDelayedTextChanged: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { _, _, _, _ -> },
    private val onDefaultAfterTextChanged: (s: Editable?) -> Unit = { _ -> },
) : TextWatcher {
    private val delayedPeriod: Long = 500

    private val coroutineScope = lifecycle.coroutineScope

    private var queryJob: Job? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        onDefaultBeforeTextChanged(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        queryJob?.cancel()
        queryJob = coroutineScope.launch {
            s?.let {
                delay(delayedPeriod)
                onDelayedTextChanged(s, start, before, count)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        onDefaultAfterTextChanged(s)
    }
}