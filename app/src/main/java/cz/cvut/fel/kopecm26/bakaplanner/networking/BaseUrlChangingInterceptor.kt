package cz.cvut.fel.kopecm26.bakaplanner.networking

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.PlannerApplication
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlChangingInterceptor : Interceptor {

    private val host: HttpUrl
        get() = Prefs.getString(Constants.Prefs.BASE_URL, PlannerApplication.BASE_URL_PLACEHOLDER).toHttpUrl()

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = host.let {
            val newUrl = chain.request().url.newBuilder()
                .scheme(it.scheme)
                .host(it.toUrl().toURI().host)
                .port(it.port)
                .build()

            return@let chain.request().newBuilder()
                .url(newUrl)
                .build()
        }

        return chain.proceed(newRequest)
    }
}