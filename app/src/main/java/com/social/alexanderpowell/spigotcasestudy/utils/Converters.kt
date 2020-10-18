package com.social.alexanderpowell.spigotcasestudy.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converters {

    @TypeConverter
    fun fromString(value: String): HashMap<String, String> {
        val listType: Type = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromMap(map: HashMap<String, String>): String {
        return Gson().toJson(map)
    }

}