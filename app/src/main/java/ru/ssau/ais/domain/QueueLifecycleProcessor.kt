package ru.ssau.ais.domain

import kotlinx.coroutines.flow.Flow
import ru.ssau.ais.model.ChartState
import ru.ssau.ais.model.QueueParametersInit

interface QueueLifecycleProcessor {
    val chartStateFlow: Flow<ChartState>
    val efficientFlow: Flow<Double>
    val currentTimeFlow: Flow<Double>
    fun start(init: QueueParametersInit? = null)
    fun pause()
}
