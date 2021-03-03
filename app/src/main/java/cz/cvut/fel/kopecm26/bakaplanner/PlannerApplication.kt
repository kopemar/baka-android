package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.github.pedrovgs.lynx.LynxShakeDetector
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
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
        initPrefs()

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

    private fun initPrefs() {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

        preferences = EncryptedSharedPreferences.create(
            "encrypted_prefs", // fileName
            masterKeyAlias, // masterKeyAlias
            this, // context
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, // prefKeyEncryptionScheme
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM // prefvalueEncryptionScheme
        )
    }

    companion object {
        const val BASE_URL_PLACEHOLDER = "http://10.0.0.7:3000/"

        lateinit var preferences: SharedPreferences private set
    }
}
