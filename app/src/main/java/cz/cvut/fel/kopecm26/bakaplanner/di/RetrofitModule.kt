package cz.cvut.fel.kopecm26.bakaplanner.di

import cz.cvut.fel.kopecm26.bakaplanner.PlannerApplication
import cz.cvut.fel.kopecm26.bakaplanner.datasource.RemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.networking.ApiDescription
import cz.cvut.fel.kopecm26.bakaplanner.networking.RetrofitRemoteDataSource
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.networking.BaseUrlChangingInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule: Module = module {
    factory { provideOkHttpClient() }
    factory { provideApi(get()) }
    single { provideRetrofit(get()) }
    single { initDataSource(get()) }
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
    return Retrofit.Builder().baseUrl(PlannerApplication.BASE_URL_PLACEHOLDER)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create().asLenient().withNullSerialization())
        .build()
}

private fun initDataSource(apiDescription: ApiDescription): RemoteDataSource = RetrofitRemoteDataSource(apiDescription)