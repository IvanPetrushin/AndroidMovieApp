package ru.fintech.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.fintech.entities.dao.FilmInfo
import ru.fintech.entities.dao.FilmInfoDAO
import ru.fintech.utils.TypeConvertersUtil

@Database(entities = [FilmInfo::class], version = 1, exportSchema = false)
@TypeConverters(TypeConvertersUtil::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filmInfoDao(): FilmInfoDAO
}