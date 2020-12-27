package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import android.content.ContextWrapper
import com.github.pedrovgs.lynx.LynxShakeDetector
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.di.databaseModule
import cz.cvut.fel.kopecm26.bakaplanner.di.networkModule
import cz.cvut.fel.kopecm26.bakaplanner.di.repositoryModule
import cz.cvut.fel.kopecm26.bakaplanner.di.sharedViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PlannerApplication : Application() {

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

        if (BuildConfig.DEBUG) {
            initLynxLog()
        }
    }

    private fun initLynxLog() {
        LynxShakeDetector(this).init()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@PlannerApplication)
            modules(databaseModule, networkModule, repositoryModule, sharedViewModelModule)
        }
    }

    companion object {
        const val BASE_URL_PLACEHOLDER = "http://10.0.0.7:3000/"
    }

}