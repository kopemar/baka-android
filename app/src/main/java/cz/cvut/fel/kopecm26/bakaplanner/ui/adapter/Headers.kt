package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

enum class Headers(@StringRes val titleRes: Int, @DrawableRes val imageRes: Int?) {
    ACTIVE_CONTRACTS(R.string.active_contracts, R.drawable.ic_contract_active),
    INACTIVE_CONTRACTS(R.string.inactive_contracts, R.drawable.ic_contract_inactive),
    INFO(R.string.information, R.drawable.ic_info),
    ACTIONS(R.string.actions, R.drawable.ic_actions)
}
