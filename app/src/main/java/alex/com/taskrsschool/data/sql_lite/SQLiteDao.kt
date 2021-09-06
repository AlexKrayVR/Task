package alex.com.taskrsschool.data.sql_lite

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.*
import alex.com.taskrsschool.data.room.Human
import alex.com.taskrsschool.data.room.HumanDao
import android.content.ContentValues
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SQLiteDao @Inject constructor(private val sqlLite: SQLiteHelper) : DatabaseContract {

    override suspend fun deleteAllHumans() {
        sqlLite.writableDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    override fun getHumans(order: SortOrder): Flow<List<Human>> = when (order) {
        SortOrder.BY_DEFAULT -> getHumansWithoutSort()
        SortOrder.BY_NAME -> getHumansSortedByName()
        SortOrder.BY_AGE -> getHumansSortedByAge()
        SortOrder.BY_PROFESSION -> getHumansSortedByProfession()
    }

    private fun getHumansSorted(order: String?): Flow<List<Human>> {
        logDebug("SQLiteDaoImpl: sort order-$order")
        val list = mutableListOf<Human>()
        val cursor = sqlLite.readableDatabase.query(
            TABLE_NAME,
            arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_AGE,
                COLUMN_PROFESSION,
                COLUMN_COLOR
            ),
            null,
            null,
            null,
            null,
            order?: "$order ASC"
        )
        with(cursor) {
            while (moveToNext()) {
                list.add(
                    Human(
                        getInt(0),
                        getString(1),
                        getInt(2),
                        getString(3),
                        getInt(4)
                    )
                )
            }
        }
        cursor.close()
        return flowOf(list)
    }

    override fun getHumansWithoutSort(): Flow<List<Human>> = getHumansSorted(null)

    override fun getHumansSortedByName(): Flow<List<Human>> = getHumansSorted(COLUMN_NAME)

    override fun getHumansSortedByAge(): Flow<List<Human>> = getHumansSorted(COLUMN_AGE)

    override fun getHumansSortedByProfession(): Flow<List<Human>> =
        getHumansSorted(COLUMN_PROFESSION)

    override suspend fun insert(human: Human) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, human.name)
            put(COLUMN_AGE, human.age)
            put(COLUMN_PROFESSION, human.profession)
            put(COLUMN_COLOR, human.color)
        }
        sqlLite.writableDatabase.insert(TABLE_NAME, null, values)
    }

    override suspend fun delete(human: Human) {
        sqlLite.writableDatabase.delete(TABLE_NAME, "id = ?", arrayOf(human.id.toString()))
    }

    override suspend fun update(human: Human) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, human.name)
            put(COLUMN_AGE, human.age)
            put(COLUMN_PROFESSION, human.profession)
            put(COLUMN_COLOR, human.color)
        }
        sqlLite.writableDatabase.update(TABLE_NAME,values, "id = ?",arrayOf(human.id.toString()))
    }
}