package com.aritmaplay.app.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.aritmaplay.app.R

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setBackgroundResource(R.drawable.bg_email_edit_text_default)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setBackgroundResource(R.drawable.bg_email_edit_text_filled)
                } else {
                    setBackgroundResource(R.drawable.bg_email_edit_text_default)
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(s).matches() && s.isNotEmpty()) {
                    error = context.getString(R.string.error_email)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}