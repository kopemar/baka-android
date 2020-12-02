package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import com.pixplicity.easyprefs.library.Prefs

object PrefsUtils {
    fun getPrefsStringOrNull(key: String): String? = Prefs.getString(key, null)
}
