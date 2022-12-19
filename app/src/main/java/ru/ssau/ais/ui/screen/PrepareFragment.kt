package ru.ssau.ais.ui.screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import ru.ssau.ais.R
import ru.ssau.ais.databinding.FragmentPrepareBinding
import ru.ssau.ais.model.*
import java.lang.Exception
import ru.ssau.ais.Defaults

class PrepareFragment : Fragment() {

    private lateinit var binding: FragmentPrepareBinding

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrepareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.timeAcceleration.setText(Defaults.timeAcceleration.toString())

        binding.beta.setText(Defaults.beta.toString())
        binding.lambda.setText(Defaults.lambda.toString())
        binding.modelTime.setText(Defaults.modelTime.toString())
        binding.linesCount.setText(Defaults.linesCount.toString())
        binding.queueSize.setText(Defaults.queueSize.toString())

        binding.buttonNext.setOnClickListener {
            val (modelTime, linesCount, queueSize) = listOf(
                binding.modelTimeLayout to binding.modelTime,
                binding.linesCountLayout to binding.linesCount,
                binding.queueSizeLayout to binding.queueSize,
            ).map { (layout, input) ->
                try {
                    input.text.toString().toLong()
                } catch (exception: Exception) {
                    layout.error = getString(R.string.error_value_int)
                    Toast.makeText(requireContext(), R.string.error_toast, Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }

            val (beta, lambda, timeAcceleration) = listOf(
                binding.betaLayout to binding.beta,
                binding.lambdaLayout to binding.lambda,
                binding.timeAccelerationLayout to binding.timeAcceleration,
            ).map { (layout, input) ->
                val value: Double
                try {
                    value = input.text.toString().toDouble()
                } catch (exception: Exception) {
                    layout.error = getString(R.string.error_value)
                    Toast.makeText(requireContext(), R.string.error_toast, Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                value
            }

            viewModel.setProcessorInit(
                QueueParametersInit(
                    modelTime,
                    linesCount,
                    queueSize,
                    timeAcceleration,
                    beta,
                    lambda,
                )
            )
            navigateToChartFragment()
        }
    }

    private fun navigateToChartFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, ChartFragment.newInstance())
            .commit()
    }

    companion object {
        fun newInstance() = PrepareFragment()
    }
}
