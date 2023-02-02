package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.vt.cs5254.multiquiz.databinding.ActivityMainBinding
import kotlin.random.Random

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    // Name: Cesar Smokowski
    // PID: cesar8800

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()
    private lateinit var buttons: Array<Button>

    private val resultsLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.resetPressed =
                result.data?.getBooleanExtra(RESET_ALL_PRESSED, false) ?: false
            quizViewModel.resetAllQuestions()
            quizViewModel.moveToNext()
            updateQuestion()
        }
        else {
            quizViewModel.moveToNext()
            updateQuestion()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called (in MultiQuiz)")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        buttons = arrayOf(binding.answer0Button, binding.answer1Button, binding.answer2Button, binding.answer3Button)

        binding.hintButton.setOnClickListener { view: View ->
            quizViewModel.increaseHintsUsed()

            quizViewModel.currentAnswerList.filter { !it.isCorrect && it.isEnabled }
                .random()
                .let {
                    it.isEnabled = false
                    it.isSelected = false
                }
            updateView()

        }

        binding.submitButton.setOnClickListener { view: View ->
            if (quizViewModel.currentAnswerList.any {it -> it.isCorrect && it.isSelected}) {
                quizViewModel.increaseCorrectAnswers()
            }

            if (quizViewModel.hasMoreQuestions()) {
                quizViewModel.moveToNext()
                updateQuestion()
            } else {
                val intent = ResultActivity.newIntent(this, quizViewModel.currentAnswerList.size, quizViewModel.correctAnswerInt, quizViewModel.hintsUsed)
                resultsLauncher.launch(intent)
                updateQuestion()
            }
        }
        updateQuestion()
    }

    private fun updateView()
    {
        quizViewModel.currentAnswerList.zip(buttons)
            .forEach { (answer, button) ->
                button.isSelected = answer.isSelected
                button.isEnabled = answer.isEnabled
                button.updateColor()
            }

        binding.hintButton.isEnabled = quizViewModel.currentAnswerList.any { !it.isCorrect && it.isEnabled }
        binding.submitButton.isEnabled = quizViewModel.currentAnswerList.any { it.isSelected }
    }

    private fun updateQuestion()
    {
        binding.questionTextView.setText(quizViewModel.currentQuestionText)
        quizViewModel.currentAnswerList.zip(buttons)
            .forEach { (answer, button) ->
                button.setText(answer.textResId)
                button.setOnClickListener {
                    answer.isSelected = !answer.isSelected
                    quizViewModel.currentAnswerList.filter { it != answer}
                        .forEach { deselectedAnswer ->
                            deselectedAnswer.isSelected = false
                        }
                    updateView()
                }
            }
        updateView()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "-------- onPause -------- ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "-------- onDestroy -------- ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "-------- onStop -------- ")
    }
}

