package alex.com.taskrsschool.common

//key for fragment result: editor fragment -> human fragment
const val EDITOR_RESULT_KEY = "editor_result"

//values for saved human confirmation messages
const val ADD_HUMAN_RESULT_OK = 1
const val EDIT_HUMAN_RESULT_OK = 2

//empty string is empty string:3
const val EMPTY_STRING = ""

//if color is not selected -> assign color of current theme
const val DEFAULT_HUMAN_CARD_COLOR = 0


enum class SortOrder() {
    BY_DEFAULT, BY_NAME, BY_AGE, BY_PROFESSION
}

enum class TypeDB {
    ROOM, SQL_LITE
}


