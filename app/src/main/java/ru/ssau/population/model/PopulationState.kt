package ru.ssau.ais.model

// одна популяция в конкретный момент времени
interface PopulationState {
    val population: PopulationStats
    val count: Long
}