package com.ilaftalkful.mobileonthego.bindingAdaptor

import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import com.ilaftalkful.mobileonthego.listener.OnOkInSoftKeyboardListener
import com.ilaftalkful.mobileonthego.utilities.DateUtil


@BindingAdapter("clicks")
    fun listenClicks(spinner: AppCompatSpinner, result: ObservableField<String>) {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                result.set(parent?.getItemAtPosition(position) as String)
            }
        }

    }

@BindingAdapter("android:adapter")
fun setAutoCompleteAdapter(textView: AutoCompleteTextView, adapter: ArrayAdapter<*>) {
    textView.setAdapter(adapter)
}

@BindingAdapter("bindServerDate")
fun bindServerDate(@NonNull view: View?, date: String?) {
   DateUtil.getDateToFromat(date)
}

@BindingAdapter("onOkInSoftKeyboard") // I like it to match the listener method name
fun setOnOkInSoftKeyboardListener(
    view: TextView,
    listener: OnOkInSoftKeyboardListener?
) {
    if (listener == null) {
        view.setOnEditorActionListener(null)
    } else {
        view.setOnEditorActionListener(OnEditorActionListener { v, actionId, event -> // ... solution to receiving event
            if (event.action === KeyEvent.ACTION_DOWN) {
                when (actionId) {
                    KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                        return@OnEditorActionListener true
                    }
                    else -> {
                    }
                }
            }
            return@OnEditorActionListener false
        })
    }
}