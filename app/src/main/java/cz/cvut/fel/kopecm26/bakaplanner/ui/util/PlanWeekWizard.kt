package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

enum class PlanWeekWizard(val index: Int, @StringRes val stepName: Int) {
    SELECT_DAYS(1, R.string.select_working_days),
    PLAN_DAYS(2, R.string.plan_working_days),
    REVIEW(3, R.string.review),
    ADJUST_SHIFTS(4, R.string.adjust_shifts);

    companion object {
        fun getSteps() = values().sortedBy { it.index }

    }
}