package com.aritmaplay.app.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aritmaplay.app.R
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.databinding.FragmentQuizBinding
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {
    private val viewModel by viewModels<QuizViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var isCanvasEmpty = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDrawView()
        observeViewModel()

        binding.sendButton.setOnClickListener {
            if (isCanvasEmpty) {
                Toast.makeText(requireContext(), "Canvas konsong!", Toast.LENGTH_SHORT).show()
            } else {
                val bitmap = binding.drawView.saveAsBitmap()
                viewModel.predict(bitmap, requireContext())

                Toast.makeText(requireContext(), "Data terkirim!", Toast.LENGTH_SHORT).show()
                binding.drawView.clearCanvas(needsSaving = false)
                isCanvasEmpty = true

                findNavController().navigate(R.id.action_quizFragment_to_resultFragment)
            }
        }

        binding.deleteButton.setOnClickListener {
            binding.drawView.clearCanvas(needsSaving = false)
            isCanvasEmpty = true
            Toast.makeText(requireContext(), "Canvas cleared!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.predictResult.observe(requireActivity()) { state ->
            when (state) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("HandwritingPredict", "Predicting...")
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("HandwritingPredict", "Predict: ${state.data.data}")
                    lifecycleScope.launch {
                    }
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    Log.e("HandwritingPredict", "Error: ${state.message}")
                }
            }
        }
    }

    private fun initDrawView() {
        binding.drawView.apply {
            brushSize = 55f
            canvasBackgroundColor = Color.WHITE

            drawViewPressCallback = {
                Log.d("DRAW_VIEW", "Canvas touched")
                isCanvasEmpty = false
            }

            undoStateCallback = {
                Log.d("DRAW_VIEW", "Undo button pressed")
                isCanvasEmpty = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}