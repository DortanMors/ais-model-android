package ru.ssau.ais.model

data class QueueParametersInit(
    val modelTime: Long,
    val linesCount: Long,
    val queueSize: Long,
    val accelerationTime: Double,
    val beta: Double,
    val lambda: Double,
)
