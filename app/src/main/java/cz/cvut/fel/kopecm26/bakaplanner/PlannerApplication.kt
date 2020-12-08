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
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class PlannerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initKoin()
        initLynxLog()

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
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
            modules(databaseModule, networkModule, repositoryModule)
        }
    }

    companion object {
        const val BASE_URL_PLACEHOLDER = "http://10.0.0.7:3000/"
    }

}