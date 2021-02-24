package cz.cvut.fel.kopecm26.bakaplanner.networking.model

import okhttp3.Headers

sealed class ResponseModel<T> {
    class SUCCESS<T>(var data: T? = null, val headers: Headers? = null) : ResponseModel<T>()

    class ERROR<T>(val errorType: ErrorType? = null, val data: T? = null) : ResponseModel<T>()

    class LOADING<T>(val data: T? = null) : ResponseModel<T>()
}
