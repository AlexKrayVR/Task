package alex.com.taskrsschool.data

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.data.room.Human
import kotlinx.coroutines.flow.Flow

interface DatabaseContract {

    suspend fun deleteAllHumans()
    fun getHumansWithoutSort(): Flow<List<Human>>
    fun getHumansSortedByName(): Flow<List<Human>>
    fun getHumansSortedByAge(): Flow<List<Human>>
    fun getHumansSortedByProfession(): Flow<List<Human>>
    fun getHumans(order: SortOrder): Flow<List<Human>>
    suspend fun insert(human: Human)
    suspend fun delete(human: Human)
    suspend fun update(human: Human)

}