package io.panha.rd_app.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.panha.core.model.DarkThemeConfig
import io.panha.core.model.UserData
import kotlinx.coroutines.flow.map

class RDPreferenceDataSource(
    private val userPreferences: DataStore<Preferences>,
) {

    val userData = userPreferences.data
        .map { preferences ->
            val darkThemeConfig = preferences[PreferencesKeys.DARK_THEME_CONFIG] ?: DarkThemeConfig.FOLLOW_SYSTEM.name
            UserData(DarkThemeConfig.valueOf(darkThemeConfig))
        }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME_CONFIG] = darkThemeConfig.name
        }
    }
}

private object PreferencesKeys {
    val DARK_THEME_CONFIG = stringPreferencesKey("dark_theme_config")
}