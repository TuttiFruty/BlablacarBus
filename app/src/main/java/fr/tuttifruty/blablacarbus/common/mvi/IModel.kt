package fr.tuttifruty.blablacarbus.common.mvi

import androidx.lifecycle.LiveData
import fr.tuttifruty.blablacarbus.common.Event
import kotlinx.coroutines.channels.Channel

interface IModel<S : IState, I : IIntent, T : INavigation> {
    val intents: Channel<I>
    val state: LiveData<S>
    val navigation: LiveData<Event<T>>
}