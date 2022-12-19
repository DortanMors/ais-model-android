package ru.ssau.ais.model

data class ChartState(
    val t: List<Long>,        // моменты времени
    val y: List<List<Long>>,  // список кривых (у каждой кривой список точек)
)