package cz.cvut.fel.kopecm26.bakaplanner.util.ext
import cz.cvut.fel.kopecm26.bakaplanner.PlannerApplication
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants

object PrefsUtils {
    fun getPrefsStringOrNull(key: String): String? = PlannerApplication.preferences.getString(key, null)

    fun saveUser(user: User?) = PlannerApplication.preferences.edit().putString(Constants.Prefs.USER, user.toJson()).apply()

    fun getUser(): User? = getPrefsStringOrNull(Constants.Prefs.USER)?.fromJson()

    fun putString(key: String, value: String) = PlannerApplication.preferences.edit().putString(key, value).apply()

    fun remove(key: String) = PlannerApplication.preferences.edit().remove(key).apply()
}
