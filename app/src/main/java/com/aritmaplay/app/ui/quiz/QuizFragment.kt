package com.aritmaplay.app.ui.quiz

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aritmaplay.app.R
import com.aritmaplay.app.ViewModelFactory
import com.aritmaplay.app.data.Result
import com.aritmaplay.app.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {
    private val viewModel by viewModels<QuizViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private val args: QuizFragmentArgs by navArgs()
    private var currentQuestion: Int = 1
    private var isCanvasEmpty = true

    private var startTime: Long = 0L

    private lateinit var correctSound: MediaPlayer
    private lateinit var wrongSound: MediaPlayer
    private lateinit var quizSound: MediaPlayer

    val indicators = mapOf(
        1 to R.id.indicator1,
        2 to R.id.indicator2,
        3 to R.id.indicator3,
        4 to R.id.indicator4,
        5 to R.id.indicator5,
        6 to R.id.indicator6,
        7 to R.id.indicator7,
        8 to R.id.indicator8,
        9 to R.id.indicator9,
        10 to R.id.indicator10
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val operation = args.operation
        binding.topAppBar.title = "Mode $operation"

        startTime = System.currentTimeMillis()

        initDrawView()
        observeViewModel()
        viewModel.generateNewQuestion(operation)
        binding.sendButton.setOnClickListener {
            if (isCanvasEmpty) {
                Toast.makeText(requireContext(), "Canvas konsong!", Toast.LENGTH_SHORT).show()
            } else {
                val bitmap = binding.drawView.saveAsBitmap()
                viewModel.predict(bitmap, requireContext())
            }
        }

        binding.deleteButton.setOnClickListener {
            binding.drawView.clearCanvas(needsSaving = false)
            isCanvasEmpty = true
        }

        correctSound = MediaPlayer.create(context, R.raw.sound_correct)
        wrongSound = MediaPlayer.create(context, R.raw.sound_wrong)
        quizSound = MediaPlayer.create(context, R.raw.sound_quiz)

        quizSound.isLooping = true
        quizSound.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun observeViewModel() {
        viewModel.generateNewQuestion(args.operation)
        viewModel.currentQuestion.observe(viewLifecycleOwner) { quizModel ->
            binding.tvQuestion.text = quizModel.question
        }
        viewModel.predictResult.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Result.Loading -> {
                    binding.progressBar2.visibility = View.VISIBLE
                    Log.d("HandwritingPredict", "Predicting...")
                    binding.sendButton.isEnabled = false
                    binding.deleteButton.isEnabled = false
                }

                is Result.Success -> {
                    binding.progressBar2.visibility = View.GONE
                    val predictedAnswer = state.data.data.digit
                    val correctAnswer = viewModel.currentQuestion.value?.correctAnswer
                    Log.d("HandwritingPredict", "Predict: ${state.data.data}")

                    if (correctAnswer != null) {
                        quizReview(predictedAnswer, correctAnswer)
                    }

                }

                is Result.Error -> {
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error Loading Data", Toast.LENGTH_SHORT).show()
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

    private fun quizReview(predictedAnswer: Int, correctAnswer: Int) {
        if (predictedAnswer == correctAnswer) {
            indicators[currentQuestion]?.let { binding.root.findViewById<View>(it).setBackgroundResource(R.drawable.indicator_correct) }
            playCorrectSound()
            viewModel.incrementCorrectAnswer()
            binding.tvCorrectTitle.text = "Jawaban kamu benar!"
        } else {
            indicators[currentQuestion]?.let { binding.root.findViewById<View>(it).setBackgroundResource(R.drawable.indicator_wrong) }
            wrongSound.start()
            binding.tvCorrectTitle.text = "Jawabanmu $predictedAnswer, kurang tepat!"
            binding.tvCorrectAnswer.text = "Jawaban yang benar adalah $correctAnswer"
            binding.tvCorrectAnswer.visibility = View.VISIBLE
        }

        currentQuestion++

        binding.btNextQuiz.setOnClickListener {
            binding.tvCorrectAnswer.visibility = View.INVISIBLE
            binding.linearLayoutBottom.visibility = View.GONE
            binding.drawView.clearCanvas(needsSaving = false)
            isCanvasEmpty = true
            indicators[currentQuestion]?.let { binding.root.findViewById<View>(it).setBackgroundResource(R.drawable.indicator_active) }

            binding.sendButton.isEnabled = true
            binding.deleteButton.isEnabled = true

            if (currentQuestion <= 10) {
                viewModel.generateNewQuestion(operation = args.operation)
            } else {
                val endTime = System.currentTimeMillis()
                val duration = viewModel.getDuration(startTime, endTime)
                val correctAnswerCount = viewModel.getCorrectAnswer()
                viewModel.resetCorrectAnswer()

                Log.d("QuizDuration", "Lama pengerjaan anda: $duration")

                val directToResult = QuizFragmentDirections.actionQuizFragmentToResultFragment(
                    args.operation,
                    correctAnswerCount,
                    duration
                )

                findNavController().navigate(
                    directToResult,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.quizFragment, true)
                        .build()
                )
            }
        }

        binding.linearLayoutBottom.visibility = View.VISIBLE
    }

    private fun playCorrectSound() {
        if (correctSound.isPlaying) {
            correctSound.stop()
            correctSound.reset()
            correctSound = MediaPlayer.create(context, R.raw.sound_correct)
        }
        correctSound.start()
    }

    override fun onPause() {
        super.onPause()
        if (quizSound.isPlaying) {
            quizSound.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        quizSound.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        quizSound.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        correctSound.release()
        wrongSound.release()
        _binding = null
    }
}