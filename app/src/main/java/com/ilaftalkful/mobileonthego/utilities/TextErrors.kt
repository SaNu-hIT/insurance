package com.ilaftalkful.mobileonthego.utilities

import android.text.InputFilter
import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app:errorText")
fun showError(textInpputLayout: TextInputLayout, msg:String?){
    textInpputLayout.error = msg;
}

@BindingConversion
fun booleanToVisibility(value: Boolean?) = if (value == true) View.VISIBLE else View.GONE

fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}

/*fun EditText.minMaxValues(min: Int,max: Int) {
    this.setFilters(arrayOf<InputFilter>(InputFilterMinMax(min.toFloat(), max.toFloat())))
}*/
