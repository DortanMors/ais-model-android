package ru.ssau.ais.domain

import kotlinx.coroutines.flow.Flow
import ru.ssau.ais.model.ChartState
import ru.ssau.ais.model.LifecycleInit

interface PopulationsLifecycleProcessor {
    val chartStateFlow: Flow<ChartState>
    fun start(init: LifecycleInit? = null)
    fun pause()
}
