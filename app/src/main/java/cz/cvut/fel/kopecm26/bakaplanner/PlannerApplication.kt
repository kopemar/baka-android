package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiService
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit


class PlannerApplication : Application() {


    private val networkModule: Module = module {
        factory { provideOkHttpClient() }
        factory { provideApi(get()) }
        single { provideRetrofit(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initKoin()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor {
            it.request().url().encodedPath().replace(BASE_URL_PLACEHOLDER, Prefs.getString(Constants.Prefs.BASE_URL, BASE_URL_PLACEHOLDER))
            return@addInterceptor it.proceed(it.request())
        }.build()
    }

    private fun provideApi(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = MediaType.get("application/json")

        return Retrofit.Builder().baseUrl(BASE_URL_PLACEHOLDER)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@PlannerApplication)
            modules(networkModule)
        }
    }

    companion object {
        private const val BASE_URL_PLACEHOLDER = "http://localhost/"
    }

}