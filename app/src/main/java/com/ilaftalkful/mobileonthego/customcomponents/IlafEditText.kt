package com.ilaftalkful.mobileonthego.customcomponents

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.ilaftalkful.mobileonthego.R

class IlafEdittext: AppCompatEditText {

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)
    init {
        this.setTextColor(ContextCompat.getColor(context, R.color.text_color))
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0F)
        this.setHintTextColor(ContextCompat.getColor(context, R.color.text_color))
    }
}