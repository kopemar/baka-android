package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import androidx.annotation.StringRes
import cz.cvut.fel.kopecm26.bakaplanner.R

open class ErrorType(@StringRes val messageRes: Int = R.string.unknown_error)

class UnauthorizedError : ErrorType(R.string.unauthorized)

class NoInternetError : ErrorType(R.string.no_internet_connection)

class NotFoundError : ErrorType(R.string.not_found)

class NoServerConnectionError : ErrorType(R.string.no_server_connection)
