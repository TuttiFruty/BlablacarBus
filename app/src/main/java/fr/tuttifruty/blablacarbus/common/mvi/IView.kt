package fr.tuttifruty.blablacarbus.common.mvi

interface IView<S : IState, T : IIntent, R : INavigation> {
    fun render(state: S)
    fun sendIntent(intent: T)
    fun navigateTo(navigation: R)
}