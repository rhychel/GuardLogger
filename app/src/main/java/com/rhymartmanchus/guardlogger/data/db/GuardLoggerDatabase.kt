package com.rhymartmanchus.guardlogger.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rhymartmanchus.guardlogger.data.db.models.*
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

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
                        .addCallback(object : Callback() {
                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                                GlobalScope.launch {
                                    val locations = getInstance(appContext)
                                        .areaLocationsDao()
                                        .getAllAreaLocations()
                                    if(locations.isEmpty()) {
                                        getInstance(appContext)
                                            .areaLocationsDao()
                                            .prepopulateAreaLocations(
                                                defaultLocations()
                                            )
                                    }
                                }
                            }
                        })
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

        private fun defaultLocations(): List<AreaLocationDB> =
            listOf(
                AreaLocationDB(1, "Phelan Building","."),
                AreaLocationDB(2, "Dolan Building",".."),
                AreaLocationDB(3, "Engineering Dean's Offine","..."),
                AreaLocationDB(4, "Xavier Hall","...."),
                AreaLocationDB(5, "Christ the King Chapel","....."),
                AreaLocationDB(6, "Engineering Building","......"),
                AreaLocationDB(7, "Alingal Canteen","......."),
                AreaLocationDB(8, "Main Building","........")
            )
    }

}