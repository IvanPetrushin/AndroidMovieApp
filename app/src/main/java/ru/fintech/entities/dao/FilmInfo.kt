package ru.fintech.entities.dao

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.fintech.entities.dto.DescriptionDTO

@Entity
data class FilmInfo(
    @PrimaryKey val id: Int,
    @Embedded val description: DescriptionDTO
)