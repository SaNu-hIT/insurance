package com.ilaftalkful.mobileonthego.base

import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

open class IlafBaseActivity : AppCompatActivity() {

    fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(
                currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

}