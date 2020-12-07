package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import android.content.ContextWrapper
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiDescription
import cz.cvut.fel.kopecm26.bakaplanner.networking.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.datasource.RetrofitRemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.repository.ShiftRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.networking.BaseUrlChangingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        single { initDataSource(get()) }
        single { UserRepository(get()) }
        single { ShiftRepository(get()) }
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
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(BaseUrlChangingInterceptor())
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                Constants.UserHeaders.values().forEach {
                    PrefsUtils.getPrefsStringOrNull(it.key)?.run {
                        request.addHeader(it.key, this).build()
                    }
                }
                chain.proceed(request.build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    private fun provideApi(retrofit: Retrofit) = retrofit.create(ApiDescription::class.java)

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_PLACEHOLDER)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create().asLenient().withNullSerialization())
            .build()
    }

    private fun initDataSource(apiDescription: ApiDescription): RemoteDataSource = RetrofitRemoteDataSource(apiDescription)

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