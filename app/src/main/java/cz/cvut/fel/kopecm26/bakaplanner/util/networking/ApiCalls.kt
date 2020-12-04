package cz.cvut.fel.kopecm26.bakaplanner.util.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.NoInternetError
import cz.cvut.fel.kopecm26.bakaplanner.networking.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.UnauthorizedError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

suspend fun <T, F> safeApiCall(call: suspend () -> Response<T>, converter: (T?) -> F?): ResponseModel<F> {
    return try {
        val response = call.invoke()
        if (response.isSuccessful) ResponseModel.SUCCESS(converter.invoke(response.body()), response.headers())
        else ResponseModel.ERROR(errorType = when (response.code()) {
            401 -> UnauthorizedError()
            else -> ErrorType()
        }, data = converter.invoke(response.body()))
    } catch (e: Exception) {
        ResponseModel.ERROR(mapToDomainError(e))
    }
}

fun mapToDomainError(
    remoteException: Exception
): ErrorType {
    return when (remoteException) {
        is IOException -> NoInternetError()
        is HttpException -> ErrorType()
        else -> ErrorType()
    }
}