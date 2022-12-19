package ru.ssau.ais.model

data class PopulationStateImpl(
    override val population: PopulationStats,
    override val count: Long,
) : PopulationState
