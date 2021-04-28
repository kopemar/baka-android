package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

enum class NativeOptions(@StringRes val nameRes: Int, @DrawableRes val iconRes: Int) {
    HISTORY(R.string.history, R.drawable.ic_history),
    EMPLOYEES(R.string.employees, R.drawable.ic_employee_count),
    SPECIALIZATIONS(R.string.specializations, R.drawable.ic_user_cog),
    SHOW_PROFILE(R.string.show_profile, R.drawable.ic_profile),
    SHOW_CONTRACTS(R.string.contracts, R.drawable.ic_contracts),
    SETTINGS(R.string.settings, R.drawable.ic_settings)
}