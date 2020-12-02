package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager

/**
 * Utils function to simplify creation of new Activities...
 */
inline fun <reified A: Activity> Context.startActivity()  = startActivity(Intent(this, A::class.java))

fun Activity.hideKeyboard() = (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(window.decorView.windowToken, 0)

