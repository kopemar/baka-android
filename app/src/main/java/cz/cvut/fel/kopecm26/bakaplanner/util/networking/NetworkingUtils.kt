package cz.cvut.fel.kopecm26.bakaplanner.util.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkingUtils {
    /**
     * Synchronous check for network connection
     */
    fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

}
