package com.social.alexanderpowell.spigotcasestudy.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.social.alexanderpowell.spigotcasestudy.utils.Converters
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "device_id")
    val device_id: String,
    @ColumnInfo(name = "manufacturer")
    val manufacturer: String,
    @ColumnInfo(name = "model")
    val model: String,
    @TypeConverters(Converters::class)
    @ColumnInfo(name = "parameters")
    val parameters: HashMap<String, String>
)