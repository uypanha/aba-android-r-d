package io.panha.core.data.repository

import androidx.appcompat.app.AppCompatDelegate
import io.panha.core.model.DarkThemeConfig
import io.panha.core.model.UserData
import io.panha.rd_app.core.datastore.RDPreferenceDataSource
import kotlinx.coroutines.flow.Flow

class OfflineFirstUserDataRepository(
    private val rdPreferenceDataSource: RDPreferenceDataSource
) : UserDataRepository {

    override val userData: Flow<UserData>
        get() = rdPreferenceDataSource.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        rdPreferenceDataSource.setDarkThemeConfig(darkThemeConfig)
    }
}