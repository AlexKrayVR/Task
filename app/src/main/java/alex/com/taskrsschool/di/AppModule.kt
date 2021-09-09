package alex.com.taskrsschool.di

import alex.com.taskrsschool.data.*
import alex.com.taskrsschool.data.room.HumanDatabase
import alex.com.taskrsschool.data.sql_lite.SQLiteHelper
import alex.com.taskrsschool.domain.model.Human
import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSQLiteDatabase(app: Application): SQLiteHelper =
        SQLiteHelper(app)

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application) =
        Room
            .databaseBuilder(app, HumanDatabase::class.java, HUMAN_DATABASE_NAME)
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    val initialList = listOf<Human>(
                        Human(
                            name = "Greg",
                            age = 23,
                            profession = "Doctor",
                            color = -11396881
                        ),
                        Human(
                            name = "Alex",
                            age = 44,
                            profession = "Teacher",
                            color = -17862
                        ),
                        Human(
                            name = "Mary",
                            age = 33,
                            profession = "Manager",
                            color = 0
                        )
                    )
                    for (human in initialList) {
                        val values = ContentValues().apply {
                            put(COLUMN_NAME, human.name)
                            put(COLUMN_AGE, human.age)
                            put(COLUMN_PROFESSION, human.profession)
                            put(COLUMN_COLOR, human.color)
                        }
                        db.insert(TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, values)
                    }
                }
            })
            .build()

    @Provides
    fun provideHumanDao(db: HumanDatabase) = db.humanDao()

}




