package ru.ssau.ais.model

data class Task(
    /**
     * Длительность задачи в минутах моделирования
     */
    val duration: Double,
    /**
     * Масштабированная длительность в миллисекунды процессора
     */
    val taskDelay: Long,
)
