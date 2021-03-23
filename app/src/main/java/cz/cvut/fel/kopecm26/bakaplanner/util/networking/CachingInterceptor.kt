package cz.cvut.fel.kopecm26.bakaplanner.util.networking

import android.content.Context
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CachingInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val cacheControl = CacheControl.Builder()
        val request = chain.request().newBuilder().run {

            if (NetworkingUtils.hasNetwork(context)) {
                cacheControl(cacheControl.maxAge(100, TimeUnit.SECONDS).build()).removeHeader("Pragma").build()
            } else {
                cacheControl(cacheControl.onlyIfCached().maxStale(7, TimeUnit.DAYS).build()).removeHeader("Pragma").build()
            }

        }
        return chain.proceed(request)
    }
}
