package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

enum class EmptyScreens(
    @DrawableRes val iconRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val bottomButtonTitle: Int? = null,
    @StringRes val topButtonTitle: Int? = null,
    @DrawableRes val bottomButtonIcon: Int? = null,
    @DrawableRes val topButtonIcon: Int? = null,
) {
    NO_PERIOD_UNITS(
        R.drawable.ic_calendar,
        R.string.no_specializations_in_your_organization
    ),
    NO_SPECIALIZATIONS(
        R.drawable.ic_history_empty,
        R.string.no_specializations_in_your_organization
    ),
    NOTHING(
        R.drawable.ic_mdi_home,
        R.string.no_more_plans_for_you
    ),
    NO_EMPLOYEES(
        R.drawable.ic_history_empty,
        R.string.no_employees
    );
}
