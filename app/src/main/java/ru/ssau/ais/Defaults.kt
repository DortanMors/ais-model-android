package ru.ssau.ais

import ru.ssau.ais.model.PopulationParameters
import ru.ssau.ais.model.PopulationState
import ru.ssau.ais.model.PopulationStateImpl

object Defaults {
    var timeAcceleration: Double = 100.0 // ускорение времени моделирования
    val delay: Long
        get() = (1000.0 / timeAcceleration).toLong()  // задержка отрисовки в миллисекундах
    var maxPointsAtAxis: Int = 50 // максимальное количество точек на оси в одно время

    const val modelTime: Long = 100 // время моделирования в минутах
    const val linesCount: Long = 3
    const val queueSize: Long = 1
    const val beta: Double = 0.1
    const val lambda: Double = 0.1

    private val producerPopulationState: PopulationState = PopulationStateImpl(
        PopulationParameters(
            selfReproductionFactor = 0.5,
            attackFactor = 0.0,
            defenseFactor = 10.0,
            nutrition = 1.0,
            hungerFactor = 1.0,
        ),
        count = 100,
    )

    private val predatorPopulationState: PopulationState = PopulationStateImpl(
        PopulationParameters(
            selfReproductionFactor = -0.1,
            attackFactor = 0.05,
            defenseFactor = 5.0,
            nutrition = 3.0,
            hungerFactor = 3.0,
        ),
        count = 10,
    )

    private val apexPredatorPopulationState: PopulationState = PopulationStateImpl(
        PopulationParameters(
            selfReproductionFactor = -0.2,
            attackFactor = 0.01,
            defenseFactor = 10000.0,
            nutrition = 1.0,
            hungerFactor = 5.0,
        ),
        count = 10,
    )

    val populations = listOf(
        producerPopulationState,
        predatorPopulationState,
        apexPredatorPopulationState,
    )
}
