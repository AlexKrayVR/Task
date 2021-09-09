package alex.com.taskrsschool.data.sql_lite

import alex.com.taskrsschool.data.CREATE_TABLE
import alex.com.taskrsschool.data.DATABASE_VERSION
import alex.com.taskrsschool.data.DELETE_TABLE
import alex.com.taskrsschool.data.HUMAN_DATABASE_NAME
import android.content.Context
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE)
        onCreate(db)
    }
}

