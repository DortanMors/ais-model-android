package ru.ssau.ais.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TaskLine(
    val index: Int,
    private val freedomCallback: (task: CompletedTask) -> Unit,
) {
    private var _isFree: Boolean = true
    val isFree: Boolean
        get() = _isFree

    private val coroutineScope = CoroutineScope(Dispatchers.Default + Job())

    /**
     * @param task - задача, которую нужно выполнить
     * @param startTime - время запуска задачи в минутах моделирования
     */
    fun startTask(task: Task, startTime: Double) {
        if (!_isFree) {
            throw IllegalStateException("Line is not free")
        }
        _isFree = false
        coroutineScope.launch {
            delay(task.taskDelay)
            _isFree = true
            freedomCallback(
                CompletedTask(
                    timeStart = startTime,
                    timeEnd = startTime + task.duration,
                    lineIndex = index,
                )
            )
        }
    }
}
