package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants

object PrefsUtils {
    fun getPrefsStringOrNull(key: String): String? = Prefs.getString(key, null)

    fun saveUser(user: User?) = Prefs.putString(Constants.Prefs.USER, user.toJson())
}
