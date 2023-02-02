package edu.vt.cs5254.multiquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val RESET_ALL_KEY = "RESET_ALL_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_1, listOf(Answer(R.string.question1_answer1, true, true, false),
            Answer(R.string.question1_answer2, false, true, false),
            Answer(R.string.question1_answer3, false, true, false),
            Answer(R.string.question1_answer4, false, true, false))),
        Question(R.string.question_2, listOf(Answer(R.string.question2_answer1, false, true, false),
            Answer(R.string.question2_answer2, true, true, false),
            Answer(R.string.question2_answer3, false, true, false),
            Answer(R.string.question2_answer4, false, true, false))),
        Question(R.string.question_3, listOf(Answer(R.string.question3_answer1, false, true, false),
            Answer(R.string.question3_answer2, false, true, false),
            Answer(R.string.question3_answer3, true, true, false),
            Answer(R.string.question3_answer4, false, true, false))),
        Question(R.string.question_4, listOf(Answer(R.string.question4_answer1, false, true, false),
            Answer(R.string.question4_answer2, false, true, false),
            Answer(R.string.question4_answer3, false, true, false),
            Answer(R.string.question4_answer4, true, true, false)))


    )

    private var currentIndex = 0
    private var correctAnswerCount = 0
    private var hintsUsedCount = 0

    var resetPressed: Boolean
        get() = savedStateHandle.get(RESET_ALL_KEY) ?: false
        set(value) = savedStateHandle.set(RESET_ALL_KEY, value)

    val correctAnswerInt: Int
        get() = correctAnswerCount

    val hintsUsed: Int
        get() = hintsUsedCount

    fun resetAllQuestions() {
        correctAnswerCount = 0
        hintsUsedCount = 0

        questionBank.forEach { question ->
            question.answerList.filter {it.isSelected || !it.isEnabled}.forEach {
                it.isSelected = false
                it.isEnabled = true
            }
        }
    }

    fun increaseCorrectAnswers() {
        correctAnswerCount += 1
    }

    fun increaseHintsUsed() {
        hintsUsedCount += 1
    }

    val currentAnswerList: List<Answer>
        get() = questionBank[currentIndex].answerList

    val currentQuestionText: Int
        get() = questionBank[currentIndex].questionResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun hasMoreQuestions(): Boolean {
        return currentIndex < questionBank.size - 1
    }

}