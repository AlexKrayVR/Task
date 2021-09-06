package alex.com.taskrsschool.data.sql_lite

import alex.com.taskrsschool.data.*
import alex.com.taskrsschool.data.room.Human
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) : SQLiteOpenHelper(
    context,
    HUMAN_DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)

//        val values = ContentValues().apply {
//            put(COLUMN_NAME, "df")
//            put(COLUMN_AGE, 44)
//            put(COLUMN_PROFESSION, "dfg")
//            put(COLUMN_COLOR, -15316925)
//        }
//        db.insert(TABLE_NAME, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE)
        onCreate(db)
    }
}

