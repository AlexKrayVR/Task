package alex.com.taskrsschool.data.room

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.data.DatabaseContract
import alex.com.taskrsschool.data.TABLE_NAME
import alex.com.taskrsschool.domain.model.Human
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HumanDao :DatabaseContract {

    @Query("SELECT *FROM $TABLE_NAME")
    override fun getHumansWithoutSort(): Flow<List<Human>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY name ASC")
    override fun getHumansSortedByName(): Flow<List<Human>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY age ASC")
    override fun getHumansSortedByAge(): Flow<List<Human>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY profession ASC")
    override fun getHumansSortedByProfession(): Flow<List<Human>>

    override fun getHumans(order: SortOrder): Flow<List<Human>> = when (order) {
        SortOrder.BY_DEFAULT -> getHumansWithoutSort()
        SortOrder.BY_NAME -> getHumansSortedByName()
        SortOrder.BY_AGE -> getHumansSortedByAge()
        SortOrder.BY_PROFESSION -> getHumansSortedByProfession()
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(human: Human)

    @Delete()
    override suspend fun delete(human: Human)

    @Update()
    override suspend fun update(human: Human)
}