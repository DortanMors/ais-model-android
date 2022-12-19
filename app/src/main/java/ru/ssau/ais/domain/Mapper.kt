package ru.ssau.ais.domain

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import ru.ssau.ais.model.CompletedTask

fun CompletedTask.toDataSet(): LineDataSet =
    LineDataSet(
        listOf(
            Entry(timeStart.toFloat(), lineIndex.toFloat()),
            Entry(timeEnd.toFloat(), lineIndex.toFloat())
        ),
        "",
    )
