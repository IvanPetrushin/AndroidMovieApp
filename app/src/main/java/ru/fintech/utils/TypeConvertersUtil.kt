package ru.fintech.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.fintech.entities.dto.Country
import ru.fintech.entities.dto.Genre

fun <T> T.toJson(): String = Gson().toJson(this)

class TypeConvertersUtil {
    @TypeConverter
    fun genreToJson(it: List<Genre>) = it.toJson()

    @TypeConverter
    fun genreFromJson(src: String): List<Genre> =
        Gson().fromJson(src, object : TypeToken<List<Genre>>() {}.type)

    @TypeConverter
    fun countryToJson(it: List<Country>) = it.toJson()

    @TypeConverter
    fun countryFromJson(src: String): List<Country> =
        Gson().fromJson(src, object : TypeToken<List<Country>>() {}.type)
}
