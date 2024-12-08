package com.fsacchi.schoolmate.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fsacchi.schoolmate.data.local.dao.UserDao
import com.fsacchi.schoolmate.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {

                instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "appDBSchoolMate"
                    ).build()
            }

            return instance as AppDatabase
        }

        fun destroyInstance() {
            instance = null
        }
    }
}
