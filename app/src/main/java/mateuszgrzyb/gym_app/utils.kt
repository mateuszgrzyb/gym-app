@file:OptIn(ExperimentalMaterial3Api::class)

package mateuszgrzyb.gym_app

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import mateuszgrzyb.gym_app.db.Workout

fun DrawerValue.toBoolean(): Boolean = when (this) {
    DrawerValue.Closed -> false
    DrawerValue.Open -> true
}

fun Boolean.toDrawerValue(): DrawerValue = if (this) { DrawerValue.Open } else { DrawerValue.Closed }

enum class WeightUnit {
    KG,
    LB,
}

fun displayName(workout: Workout?): String =
    if (workout == null) {
        "Empty"
    } else {
        workout.name ?: "Workout #${workout.id}"
    }

val String.cap: String
    get() = this.replaceFirstChar { c -> c.uppercase() }