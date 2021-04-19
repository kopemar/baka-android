package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Utils function to simplify creation of new Activities...
 */
inline fun <reified A : Activity> Context.startActivity() =
    startActivity(Intent(this, A::class.java))

fun Activity.hideKeyboard() =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(window.decorView.windowToken, 0)

fun Activity.showKeyboard(view: View) =
    (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .showSoftInput(view, InputMethodManager.SHOW_FORCED)

fun Activity.finishWithResult(result: Int) {
    setResult(result)
    finish()
}

fun Activity.finishWithOkResult() {
    finishWithResult(Activity.RESULT_OK)
}

inline fun <reified T : Fragment> FragmentActivity.getFragment(@IdRes holderId: Int) =
    supportFragmentManager.findFragmentById(holderId)?.childFragmentManager
        ?.fragments?.firstOrNull { it is T } as? T