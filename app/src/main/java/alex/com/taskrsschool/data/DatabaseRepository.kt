package alex.com.taskrsschool.data

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.preferences.PreferencesManager
import alex.com.taskrsschool.domain.model.Human
import alex.com.taskrsschool.data.room.HumanDao
import alex.com.taskrsschool.data.sql_lite.SQLiteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseRepository @Inject constructor(
    private val humansSQLite: SQLiteDao,
    private var humansRoom: HumanDao,
    private val preferencesManager: PreferencesManager
) {

    private var humansBD: DatabaseContract = humansSQLite

    val job = CoroutineScope(Dispatchers.IO).launch {
        preferencesManager.typeDB.collect {
            logDebug("typeDB in DatabaseRepository: $it")
            when (it) {
                TypeDB.ROOM.name -> humansBD = humansRoom
                TypeDB.SQL_LITE.name -> humansBD = humansSQLite
            }
        }
    }

    private val _humansFlow =
        combine(
            preferencesManager.sortOrder,
            preferencesManager.trigger,
        ) { sortOrder, trigger ->
            Pair(sortOrder, trigger)
        }
            .flatMapLatest { (sortOrder, trigger) ->
                logDebug("sortOrder: $sortOrder\t ")
                logDebug("trigger: $trigger\t ")
                getHumans(SortOrder.valueOf(sortOrder))
            }

    var humansFlow = _humansFlow

    private fun getHumans(order: SortOrder): Flow<List<Human>> {
        return humansBD.getHumans(order)
    }

    suspend fun insert(human: Human) {
        humansBD.insert(human)
    }

    suspend fun delete(human: Human) {
        humansBD.delete(human)
    }

    suspend fun update(human: Human) {
        humansBD.update(human)
    }
}