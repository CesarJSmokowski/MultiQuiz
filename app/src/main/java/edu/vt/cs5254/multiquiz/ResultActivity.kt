package edu.vt.cs5254.multiquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import edu.vt.cs5254.multiquiz.databinding.ActivityResultBinding

private const val TAG = "ResultActivity"

private const val EXTRA_TC = "edu.vt.cs5254.multiquiz.total_questions"
private const val EXTRA_HU = "edu.vt.cs5254.multiquiz.hints_used"
private const val EXTRA_CA = "edu.vt.cs5254.multiquiz.correct_answers"

const val RESET_ALL_PRESSED = "edu.vt.cs5254.multiquiz.reset_all"

class ResultActivity : AppCompatActivity() {

    private val vm: ResultViewModel by viewModels()
    private val quizViewModel: QuizViewModel by viewModels()

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.resetAllButton.setOnClickListener { view: View ->
            //binding.resetAllButton.isEnabled = false
            vm.resetWasClicked()
            binding.resetAllButton.isEnabled = !vm.resetButtonValue

            intent = newIntent(this,0,0, 0) //TEST
            updateView()
            quizViewModel.resetAllQuestions()
            setResetAllResults(true) //Possibly switch order with updateView?
        }

        updateView()
    }

    private fun setResetAllResults(reset_all: Boolean) {
        val data = Intent().apply {
            putExtra(RESET_ALL_PRESSED, reset_all)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun updateView()
    {
        binding.correctAnswersValue.text = intent.getStringExtra(EXTRA_CA)
        binding.hintsUsedValue.text = intent.getStringExtra(EXTRA_HU)
        binding.totalQuestionsValue.text = intent.getStringExtra(EXTRA_TC)
    }

    companion object {

        fun newIntent(context: Context, totQuest: Int, correctAnswers: Int, hintsUsed: Int): Intent {
            return Intent(context, ResultActivity::class.java).apply {
                putExtra(EXTRA_TC, totQuest.toString())
                putExtra(EXTRA_CA, correctAnswers.toString())
                putExtra(EXTRA_HU, hintsUsed.toString())
            }
        }
    }
}
