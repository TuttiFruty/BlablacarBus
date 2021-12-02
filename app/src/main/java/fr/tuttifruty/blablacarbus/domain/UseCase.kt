package fr.tuttifruty.blablacarbus.domain

interface UseCase<I : UseCase.InputValues?, O : Result<UseCase.OutputValues?>> {
    suspend operator fun invoke(input: I? = null): O
    interface InputValues
    interface OutputValues
}