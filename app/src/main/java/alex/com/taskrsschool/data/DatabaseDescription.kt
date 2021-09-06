package alex.com.taskrsschool.data

const val HUMAN_DATABASE_NAME = "human_database"
const val DATABASE_VERSION = 1
const val TABLE_NAME = "humans"

const val COLUMN_ID = "id"
const val COLUMN_NAME = "name"
const val COLUMN_AGE = "age"
const val COLUMN_PROFESSION = "profession"
const val COLUMN_COLOR = "color"
const val CREATE_TABLE =
    "CREATE TABLE $TABLE_NAME " +
            "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "$COLUMN_NAME TEXT, " +
            "$COLUMN_AGE INTEGER, " +
            "$COLUMN_PROFESSION TEXT, " +
            "$COLUMN_COLOR INTEGER NOT NULL)"

const val DELETE_TABLE = "DROP TABLE IF EXIST $TABLE_NAME"



