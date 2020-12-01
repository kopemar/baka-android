package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import android.content.ContextWrapper
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import com.squareup.moshi.Moshi
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiService
import cz.cvut.fel.kopecm26.bakaplanner.networking.BaseUrlChangingInterceptor
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class PlannerApplication : Application() {

    private val networkModule: Module = module {
        factory { provideOkHttpClient() }
        factory { provideApi(get()) }
        single { provideRetrofit(get()) }

        single { UserRepository(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initKoin()

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(BaseUrlChangingInterceptor()).build()
    }

    private fun provideApi(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_PLACEHOLDER)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create().asLenient().withNullSerialization())
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
        const val BASE_URL_PLACEHOLDER = "http://10.0.0.7:3000/"
    }

}