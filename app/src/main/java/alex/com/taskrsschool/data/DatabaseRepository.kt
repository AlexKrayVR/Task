package alex.com.taskrsschool.data

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.preferences.PreferencesManager

import alex.com.taskrsschool.data.room.Human
import alex.com.taskrsschool.data.room.HumanDao
import alex.com.taskrsschool.data.sql_lite.SQLiteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
    private var humansRoom: HumanDao,
    private val humansSQLite: SQLiteDao,
    private val preferencesManager: PreferencesManager,
) {

    private var humansBD: DatabaseContract = humansRoom

    val job = CoroutineScope(Dispatchers.Default).launch {
        preferencesManager.typeDB.collect {
            logDebug("collect typeDB: $it")
            when (it) {
                TypeDB.ROOM.name -> humansBD = humansRoom
                TypeDB.SQL_LITE.name -> humansBD = humansSQLite
            }
        }
    }

     val humansSortedFlow = preferencesManager.sortOrder.flatMapLatest {
        getHumans(SortOrder.valueOf(it))
    }

    private fun getHumans(order: SortOrder): Flow<List<Human>> {
        return humansBD.getHumans(order)
    }

    suspend fun insert(human: Human) {
        humansBD.insert(human)
    }

    suspend fun deleteAllHumans() {
        humansBD.deleteAllHumans()
    }

    suspend fun delete(human: Human) {
        humansBD.delete(human)
    }

    suspend fun update(human: Human) {
        humansBD.update(human)
    }
}