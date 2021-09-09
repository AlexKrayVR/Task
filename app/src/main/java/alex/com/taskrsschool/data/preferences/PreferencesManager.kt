package alex.com.taskrsschool.data.preferences

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(HUMAN_PREFERENCES)

    val sortOrder: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DEFAULT.name
    }

    val typeDB: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.TYPE_DB] ?: TypeDB.ROOM.name
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateTypeDB(typeDB: TypeDB) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TYPE_DB] = typeDB.name
        }
    }


    suspend fun updateTrigger(str: String) {
        dataStore.edit { preferences ->
            if (preferences[PreferencesKeys.UPDATE_DB] == str) {
                preferences[PreferencesKeys.UPDATE_DB] = "trigger"
            } else {
                preferences[PreferencesKeys.UPDATE_DB] = str
            }
        }
    }

    val trigger: Flow<String> = dataStore.data.map {
        it[PreferencesKeys.UPDATE_DB] ?: ""
    }
}

object PreferencesKeys {
    val SORT_ORDER = preferencesKey<String>(SORT_ORDER_KEY)
    val TYPE_DB = preferencesKey<String>(TYPE_DB_KEY)

    val UPDATE_DB = preferencesKey<String>(TRIGGER_KEY)
}

const val HUMAN_PREFERENCES = "human_preferences"
const val TYPE_DB_KEY = "db_type"
const val SORT_ORDER_KEY = "sort_order"


const val TRIGGER_KEY = "trigger"