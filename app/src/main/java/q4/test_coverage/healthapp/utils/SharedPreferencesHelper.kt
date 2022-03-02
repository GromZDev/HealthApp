package q4.test_coverage.healthapp.utils

import android.content.Context

private const val APP_PREFERENCES = "prefs"

class SharedPreferencesHelper(context: Context) {

    private val preferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    companion object {
        private const val FIRST_OPEN_KEY = "FIRST_OPEN"
        private const val CURRENT_DAY_KEY = "CURRENT_DAY"
    }

    var currentDate: String? = null
        set(value) {
            field = value
            preferences.edit().putString(CURRENT_DAY_KEY, value).apply()
        }
        get() = preferences.getString(CURRENT_DAY_KEY, null)

    var isFirstOpen = true
        set(value) {
            field = value
            preferences.edit().putBoolean(FIRST_OPEN_KEY, value).apply()
        }
        get() = preferences.getBoolean(FIRST_OPEN_KEY, true)
}