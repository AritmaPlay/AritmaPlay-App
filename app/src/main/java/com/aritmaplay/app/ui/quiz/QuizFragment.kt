package com.aritmaplay.app.ui.quiz

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aritmaplay.app.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {
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

        binding.sendButton.setOnClickListener {
            if (isCanvasEmpty) {
                Toast.makeText(requireContext(), "Canvas konsong!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Data terkirim!", Toast.LENGTH_SHORT).show()
                binding.drawView.clearCanvas(needsSaving = false)
                isCanvasEmpty = true
            }
        }

        binding.deleteButton.setOnClickListener {
            binding.drawView.clearCanvas(needsSaving = false)
            isCanvasEmpty = true
            Toast.makeText(requireContext(), "Canvas cleared!", Toast.LENGTH_SHORT).show()
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