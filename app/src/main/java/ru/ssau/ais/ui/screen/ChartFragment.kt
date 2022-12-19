package ru.ssau.ais.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.data.LineData
import kotlinx.coroutines.launch
import ru.ssau.ais.databinding.FragmentChartBinding

class ChartFragment : Fragment() {

    private lateinit var binding: FragmentChartBinding

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            lifecycleScope.launch {
                viewModel.chartStateFlow.collect { chartState ->
                    binding.chart.data = LineData(chartState.lineDataSets)
                    binding.chart.invalidate()
                }
                viewModel.efficientFlow.collect { efficient ->
                    binding.efficient.text = efficient.toString()
                }
                viewModel.currentTimeFlow.collect { currentTime ->
                    binding.currentTime.text = currentTime.toString()
                }
            }
        }
        binding.startPause.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.start()
            } else {
                viewModel.pause()
            }
        }
    }

    companion object {
        fun newInstance() = ChartFragment()
    }
}
