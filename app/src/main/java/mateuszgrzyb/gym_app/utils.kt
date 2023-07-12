@file:OptIn(ExperimentalMaterial3Api::class)

package mateuszgrzyb.gym_app

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import mateuszgrzyb.gym_app.db.Workout
import java.math.RoundingMode
import java.text.DecimalFormat

fun DrawerValue.toBoolean(): Boolean = when (this) {
    DrawerValue.Closed -> false
    DrawerValue.Open -> true
}

fun Boolean.toDrawerValue(): DrawerValue = when (this) {
    true -> DrawerValue.Open
    false -> DrawerValue.Closed
}

enum class WeightUnit {
    KG,
    LB,
}

@Composable
fun displayName(workout: Workout?): String =
    when  {
        workout == null -> stringResource(R.string.empty)
        workout.name?.isNotBlank() == true -> workout.name
        else -> stringResource(R.string.workout_name, workout.id)
    }

val String.cap: String
    get() = this.replaceFirstChar { c -> c.uppercase() }

fun Double.fmt(digits: Int) = "%.${digits}f".format(this)

val Double.round: String
    get() = DecimalFormat("#.##").apply {
        roundingMode = RoundingMode.HALF_EVEN
    }.format(this)

