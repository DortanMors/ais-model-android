package ru.ssau.ais.domain

import kotlin.math.ln
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ssau.ais.Defaults
import ru.ssau.ais.model.ChartState
import ru.ssau.ais.model.CompletedTask
import ru.ssau.ais.model.QueueParametersInit
import ru.ssau.ais.model.Task
import ru.ssau.ais.model.TaskLine

class QueueLifecycleProcessorImpl : QueueLifecycleProcessor {

    private lateinit var random: Random

    private lateinit var parameters: QueueParametersInit

    private var completedTasksCount: Int = 0

    private var cancelledTasksCount: Int = 0

    private var taskLines: List<TaskLine> = listOf()

    private val tasksQueue: MutableList<Task> = mutableListOf()

    private val _currentTimeFlow = MutableStateFlow(0.0)

    override val currentTimeFlow: Flow<Double>
        get() = _currentTimeFlow

    private val _efficientFlow = MutableStateFlow(1.0)

    /**
     * Поток значения "Эффективность"
     */
    override val efficientFlow: Flow<Double> = _efficientFlow

    /**
     * Мьютекс для синхронизации доступа к [taskLines], [tasksQueue], [efficientFlow], [chartStateFlow] и [completedTasksCount]
     */
    private val dispatcherMutex = Mutex()
    /**
     * Внутренний поток состояний системы
     */
    private val _chartStateFlow = MutableStateFlow(ChartState(emptyList()))

    /**
     * Поток состояний системы, разница между двумя последовательными состояниями = timeStep
     */
    override val chartStateFlow: Flow<ChartState> = _chartStateFlow

    /**
     * Область действия корутины расчёта
     */
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Текущая работа по непрерывному вычислению
     */
    private var job: Job? = null

    /**
     * Запуск непрерывного вычисления
     * @param init начальные параметры, чтобы начать расчёт заново. Если null, то возобновить приостановленный расчёт
     */
    override fun start(init: QueueParametersInit?) {
        job = coroutineScope.launch {
            init?.also { initialChartState ->
                random = Random(System.currentTimeMillis())
                taskLines = (0 until initialChartState.linesCount).map { index ->
                    TaskLine(
                        index = index.toInt(),
                        freedomCallback = ::onTaskCompleted,
                    )
                }
                _currentTimeFlow.emit(0.0)
                parameters = initialChartState
            }
            while (_currentTimeFlow.value < parameters.modelTime) {
                val minutesPeriod = generateTaskPeriod()
                delay(minutesToDelay(minutesPeriod))
                _currentTimeFlow.emit(_currentTimeFlow.value + minutesPeriod)
                val taskDuration = generateTaskDuration()
                val task = Task(taskDuration, minutesToDelay(taskDuration))
                selectLineAndTryStartTask(task, _currentTimeFlow.value)
            }
        }
    }

    /**
     * Отмена непрерывного вычисления
     */
    override fun pause() {
        job?.cancel()
    }

    private fun minutesToDelay(minutes: Double): Long {
        return (minutes * 60 * 1000 / Defaults.timeAcceleration).toLong()
    }

    private fun generateTaskPeriod(): Double =
        - ln(random.nextDouble()) / parameters.lambda

    private fun generateTaskDuration(): Double =
        - ln(random.nextDouble()) / parameters.beta

    private suspend fun checkQueueAndStartTask(timeStart: Double) {
        dispatcherMutex.withLock {
            tasksQueue.removeLastOrNull()?.let { queuedTask ->
                selectLineAndTryStartTask(queuedTask, timeStart)
            }
        }
    }

    private suspend fun selectLineAndTryStartTask(task: Task, timeStart: Double) {
        dispatcherMutex.withLock {
            taskLines.filter { it.isFree }.randomOrNull(random)?.startTask(task, timeStart)
                ?: if (tasksQueue.size >= parameters.queueSize) {
                    tasksQueue.add(task)
                } else {
                    ++cancelledTasksCount
                }
        }
    }

    private fun onTaskCompleted(task: CompletedTask) {
        coroutineScope.launch {
            val newDataSets = _chartStateFlow.value.lineDataSets.toMutableList().apply {
                add(task.toDataSet())
            }
            _chartStateFlow.emit(ChartState(newDataSets))
            ++completedTasksCount
            _efficientFlow.emit(completedTasksCount.toDouble() / (completedTasksCount + cancelledTasksCount))
            checkQueueAndStartTask(task.timeEnd)
        }
    }
}
