package com.alexsheiko.party.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

fun MainActivity.reactToInput() {
    inputTitle.handleInput(titleView)
    inputText.handleInput(textView)
}

fun EditText.handleInput(textView: TextView) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textView.text = s.toString()
        }
    })
}