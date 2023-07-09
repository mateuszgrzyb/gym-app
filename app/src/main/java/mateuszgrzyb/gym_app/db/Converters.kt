package mateuszgrzyb.gym_app.db

import androidx.room.TypeConverter
import mateuszgrzyb.gym_app.WeightUnit

class Converters {
    @TypeConverter
    fun toWeightUnit(value: String) = enumValueOf<WeightUnit>(value)

    @TypeConverter
    fun fromWeightUnit(value: WeightUnit) = value.name
}