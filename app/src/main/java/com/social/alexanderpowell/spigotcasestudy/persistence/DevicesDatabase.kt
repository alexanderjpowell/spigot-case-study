package com.social.alexanderpowell.spigotcasestudy.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.social.alexanderpowell.spigotcasestudy.utils.Converters

@Database(entities = [Device::class], version = 1)
@TypeConverters(Converters::class)
abstract class DevicesDatabase : RoomDatabase() {

    abstract fun deviceDao(): DeviceDao

    companion object {

        @Volatile private var INSTANCE: DevicesDatabase? = null

        fun getInstance(context: Context): DevicesDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                DevicesDatabase::class.java, "Sample.db")
                .build()
    }

}