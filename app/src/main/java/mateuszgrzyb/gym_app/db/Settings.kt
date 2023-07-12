package mateuszgrzyb.gym_app.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import mateuszgrzyb.gym_app.WeightUnit

@Entity(
    tableName = "settings"
)
data class Settings(
    @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "ads_enabled") val adsEnabled: Boolean,
    @ColumnInfo(name = "weight_unit") val weightUnit: WeightUnit,
) {
    constructor(): this(
        id = 0L,
        adsEnabled = true,
        weightUnit = WeightUnit.KG,
    )
}
