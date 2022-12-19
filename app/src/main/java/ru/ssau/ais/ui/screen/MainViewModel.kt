package ru.ssau.ais.ui.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import ru.ssau.ais.domain.QueueLifecycleProcessorImpl
import ru.ssau.ais.domain.QueueLifecycleProcessor
import ru.ssau.ais.model.ChartState
import ru.ssau.ais.model.QueueParametersInit

class MainViewModel : ViewModel() {
    private val processor: QueueLifecycleProcessor = QueueLifecycleProcessorImpl()

    private var processorInit: QueueParametersInit? = null

    val chartStateFlow: Flow<ChartState>
        get() = processor.chartStateFlow

    val efficientFlow: Flow<Double>
        get() = processor.efficientFlow

    val currentTimeFlow: Flow<Double>
        get() = processor.currentTimeFlow

    fun setProcessorInit(
        lifecycleInit: QueueParametersInit,
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