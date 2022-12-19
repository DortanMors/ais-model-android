package ru.ssau.ais.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.ssau.ais.domain.PopulationLifecycleProcessorImpl
import ru.ssau.ais.domain.PopulationsLifecycleProcessor
import ru.ssau.ais.model.ChartState
import ru.ssau.ais.model.LifecycleInit
import ru.ssau.ais.model.PopulationState

class MainViewModel : ViewModel() {
    private val processor: PopulationsLifecycleProcessor = PopulationLifecycleProcessorImpl()

    private var processorInit: LifecycleInit? = null

    val populationsStates: Flow<ChartState>
        get() = processor.chartStateFlow

    fun setProcessorInit(
        lifecycleInit: LifecycleInit,
    ) {
        processorInit = lifecycleInit
    }

    fun start() {
        processor.start(processorInit)
        processorInit = null
    }

    fun pause() {
        processor.pause()
    }
}