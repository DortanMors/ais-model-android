package ru.ssau.ais.model

import com.github.mikephil.charting.data.LineDataSet

data class ChartState(
    val lineDataSets: List<LineDataSet>
)
