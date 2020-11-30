package cz.cvut.fel.kopecm26.bakaplanner

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class PlannerApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initLogger()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

}