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
        R.drawable.ic_settings,
        R.string.no_specializations_in_your_organization
    ),
    NOTHING(
        R.drawable.ic_mdi_home,
        R.string.no_more_plans_for_you
    ),
    NO_EMPLOYEES(
        R.drawable.ic_employee_count,
        R.string.no_employees
    ),
    NO_SHIFTS(
        R.drawable.ic_hourglass_start,
        R.string.no_shifts
    ),
    NO_SHIFTS_HISTORY(
        R.drawable.ic_hourglass_end,
        R.string.no_shifts
    ),
    NO_SHIFTS_TO_ASSIGN(
        R.drawable.ic_briefcase,
        R.string.no_shifts_to_assign
    ),
    NO_CONTRACT(
        R.drawable.ic_contracts,
        R.string.no_contracts
    ),
    NO_EMPLOYEES_ORGANIZATION(
        R.drawable.ic_employee_count,
        R.string.no_employees_organization
    );
}
