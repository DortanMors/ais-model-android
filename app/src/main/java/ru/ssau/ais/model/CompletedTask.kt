package ru.ssau.ais.model

data class CompletedTask(
    /**
     * Время запуска задачи в минутах моделирования
     */
    val timeStart: Double,
    /**
     * Время окончания задачи в минутах моделирования
     */
    val timeEnd: Double,
    /**
     * Номер линии, выполнившей задачу
     */
    val lineIndex: Int,
)
