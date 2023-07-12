package mateuszgrzyb.gym_app.db

import androidx.room.TypeConverter
import mateuszgrzyb.gym_app.WeightUnit
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun toWeightUnit(value: String) = enumValueOf<WeightUnit>(value)

    @TypeConverter
    fun fromWeightUnit(value: WeightUnit) = value.name

    @TypeConverter
    fun toLongList(value: String) = Json.decodeFromString<List<Long>>(value)

    @TypeConverter
    fun fromLongList(value: List<Long>) = Json.encodeToString(value)
}