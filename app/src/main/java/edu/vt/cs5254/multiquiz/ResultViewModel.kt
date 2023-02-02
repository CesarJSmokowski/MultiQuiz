package edu.vt.cs5254.multiquiz

import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {

    private var resetAllClicked = false

    fun resetWasClicked() {
        resetAllClicked = true
    }

    val resetButtonValue: Boolean
        get() = resetAllClicked
}