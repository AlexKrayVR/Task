package alex.com.taskrsschool.di

import alex.com.taskrsschool.data.HUMAN_DATABASE_NAME
import alex.com.taskrsschool.data.room.HumanDatabase
import alex.com.taskrsschool.data.sql_lite.SQLiteHelper
import android.app.Application
import androidx.room.Room
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
    fun provideRoomDatabase(app: Application) =
        Room
            .databaseBuilder(app, HumanDatabase::class.java, HUMAN_DATABASE_NAME)
            .build()

    @Provides
    fun provideHumanDao(db: HumanDatabase) = db.humanDao()

    @Provides
    @Singleton
    fun provideSQLiteDatabase(app: Application): SQLiteHelper =
        SQLiteHelper(app)
}