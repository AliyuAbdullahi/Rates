package com.lek.rates.presentation.helper

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(onKeyHidden: () -> Unit = {}) {
    val inputManager: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    var view: View? = this.currentFocus
    if (view == null) {
        view = View(this)
    }
    inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0)
    onKeyHidden()
}
