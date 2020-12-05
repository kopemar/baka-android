package cz.cvut.fel.kopecm26.bakaplanner.util.networking

import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.NoInternetError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.UnauthorizedError
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/**
 * A fun to make safeApiCall
 * @return [ResponseModel] ([ResponseModel.SUCCESS] or [ResponseModel.ERROR])
 * @param [T] is the class of API response
 * @param [F] is desired class
 * @param [converter] should be a converter from [T] to [F] (if [T] is [F], use <b>it</b>
 */
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

/**
 * @return [Exception] mapped to [ErrorType]
 */
fun mapToDomainError(
    remoteException: Exception
): ErrorType {
    return when (remoteException) {
        is IOException -> NoInternetError()
        is HttpException -> ErrorType()
        else -> ErrorType()
    }
}