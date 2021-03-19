package cz.cvut.fel.kopecm26.bakaplanner.service

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class SchedulerFirebaseMessagingService : FirebaseMessagingService() {

    private val userRepository by inject(UserRepository::class.java)

    override fun onNewToken(token: String) {
        Logger.d( "Refreshed token: $token")
        if (PrefsUtils.getUser() != null) {
            GlobalScope.launch {
                userRepository.postFirebaseToken(token)
            }
        }
    }

    companion object {
        fun retrieveToken(onCompleted: (result: String?) -> Unit) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Logger.w("Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                onCompleted.invoke(task.result)
            })
        }
    }
}