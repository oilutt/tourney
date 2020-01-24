package br.com.commons

import android.content.Context
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.regex.Pattern

private val PASSWORD_PATTERN = "[0-9a-zA-Z\\@\\#\\$\\%\\!\\-\\_\\?\\&\\S\\+]{8,20}"

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun String?.isValidEmail(): Boolean {
    if (this == null) {
        return false
    }
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.isValidPassword(): Boolean {
    if (this == null) {
        return false
    }
    val pattern = Pattern.compile(PASSWORD_PATTERN)
    return pattern.matcher(this).matches()
}