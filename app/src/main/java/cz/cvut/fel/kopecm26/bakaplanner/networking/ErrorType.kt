package cz.cvut.fel.kopecm26.bakaplanner.networking

import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

open class ErrorType(@StringRes val messageRes: Int = R.string.unknown_error)

class UnauthorizedError: ErrorType(R.string.wrong_password)

class NoInternetError: ErrorType(R.string.no_internet_connection)