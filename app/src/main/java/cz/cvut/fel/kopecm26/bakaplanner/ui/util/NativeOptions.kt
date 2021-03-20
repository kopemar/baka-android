package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

enum class NativeOptions(@StringRes val nameRes: Int, @DrawableRes val iconRes: Int) {
    HISTORY(R.string.history, R.drawable.ic_history),

    SHOW_PROFILE(R.string.show_profile, R.drawable.ic_profile)
}