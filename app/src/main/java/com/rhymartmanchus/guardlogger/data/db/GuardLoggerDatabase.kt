package com.rhymartmanchus.guardlogger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rhymartmanchus.guardlogger.data.db.models.*

@Database(
    entities = [
        AreaLocationDB::class,
        CheckInLogDB::class,
        PatrolLocationDB::class,
        RoutePlanDB::class,
        UserDB::class,
    ],
    version = 1,
    exportSchema = true
)
abstract class GuardLoggerDatabase : RoomDatabase() {

    abstract fun areaLocationsDao(): AreaLocationsDao
    abstract fun authenticationsDao(): AuthenticationsDao
    abstract fun checkInLogsDao(): CheckInLogsDao
    abstract fun patrolLocationsDao(): PatrolLocationsDao
    abstract fun routePlansDao(): RoutePlansDao

    companion object {

        private var INSTANCE: GuardLoggerDatabase? = null
        private var IN_MEMORY_INSTANCE: GuardLoggerDatabase? = null

        fun getInstance(appContext: Context): GuardLoggerDatabase {
            if(INSTANCE == null) {
                synchronized(GuardLoggerDatabase::class) {
                    INSTANCE = Room.databaseBuilder(appContext,
                        GuardLoggerDatabase::class.java, "guard-logger-database.db")
                        .fallbackToDestructiveMigration()
                        .createFromAsset("guard_logger.db")
                        .build()
                }
            }

            return INSTANCE!!
        }

        fun getInMemoryInstance(appContext: Context): GuardLoggerDatabase {
            if(IN_MEMORY_INSTANCE == null) {
                synchronized(GuardLoggerDatabase::class) {
                    IN_MEMORY_INSTANCE = Room.inMemoryDatabaseBuilder(appContext,
                        GuardLoggerDatabase::class.java)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return IN_MEMORY_INSTANCE!!
        }

        fun clearAllTables() {
            INSTANCE?.clearAllTables()
            IN_MEMORY_INSTANCE?.clearAllTables()
        }

        fun destroyInstance() {
            INSTANCE = null
            IN_MEMORY_INSTANCE = null
        }

    }

}