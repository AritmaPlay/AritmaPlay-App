package com.aritmaplay.app.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.ViewParent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.aritmaplay.app.R
import com.google.android.material.textfield.TextInputLayout

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    init {
        setBackgroundResource(R.drawable.bg_email_edit_text_default)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val parentLayout = findParentTextInputLayout()
                if (s != null) {
                    if (s.isNotEmpty()) {
                        setBackgroundResource(R.drawable.bg_email_edit_text_filled)
                    } else {
                        setBackgroundResource(R.drawable.bg_email_edit_text_default)
                    }

                    if (s.length < 8 && s.isNotEmpty()) {
                        error = context.getString(R.string.error_password)
                        parentLayout?.endIconMode = TextInputLayout.END_ICON_NONE
                    } else {
                        error = null
                        parentLayout?.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
                    }                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun findParentTextInputLayout(): TextInputLayout? {
        var thisParent = parent
        while (thisParent is ViewParent) {
            if (thisParent is TextInputLayout) return thisParent
            thisParent = (thisParent as? View)?.parent
        }
        return null
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.hint_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}