package mateuszgrzyb.gym_app.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philjay.Frequency
import com.philjay.RRule
import com.philjay.Weekday
import com.philjay.WeekdayNum
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.data.CalendarEventData
import mateuszgrzyb.gym_app.viewmodels.CalendarViewModel
import mateuszgrzyb.gym_app.db.Workout
import mateuszgrzyb.gym_app.displayName

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun WorkoutCalendarDialog(
    calendarViewModel: CalendarViewModel = viewModel(),
    workout: Workout,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    var workoutHoursInput by remember { mutableStateOf("0") }
    var weekStartInput by remember { mutableStateOf("0") }
    var days by remember { mutableStateOf(setOf<Weekday>()) }

    var workoutHoursErrors by remember { mutableStateOf<String?>(null) }
    var weekStartErrors by remember { mutableStateOf<String?>(null) }

    val workoutTitle = displayName(workout)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onClick@ {
                    weekStartErrors = null
                    workoutHoursErrors = null

                    val workoutHours = workoutHoursInput.toIntOrNull() ?: 0;
                    val weekStart = weekStartInput.toIntOrNull()

                    if (workoutHours <= 0) {
                        workoutHoursErrors = "asdfasdf"
                    }
                    if (weekStart == null) {
                        weekStartErrors = "blablabla"
                    }

                    if (weekStartErrors != null || workoutHoursErrors != null) {
                        return@onClick
                    }

                    calendarViewModel.createEventForWorkout(
                        CalendarEventData(
                            title = workoutTitle,
                            description = workout.notes,
                            workoutHours = workoutHours,
                            weekStart = weekStart!!,
                            rRule = RRule().apply {
                                freq = Frequency.Weekly
                                for (d in days) {
                                    byDay.add(WeekdayNum(0, d))
                                }
                            }
                        )
                    )
                    onConfirm()
                }
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = {
            Text(stringResource(R.string.create_calendar_event_title))
        },
        text = {
            Column {
                TextField(
                    label = {
                        Text(stringResource(R.string.workout_hours))
                    },
                    value = workoutHoursInput,
                    onValueChange = { workoutHoursInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    supportingText = {
                        if (workoutHoursErrors != null) {
                            Text(workoutHoursErrors!!)
                        }
                    }
                )
                TextField(
                    label = {
                        Text(stringResource(R.string.workout_offset_weeks))
                    },
                    value = weekStartInput,
                    onValueChange = { weekStartInput = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    supportingText = {
                        if (weekStartErrors != null) {
                            Text(weekStartErrors!!)
                        }
                    }
                )
                Text(stringResource(R.string.training_weekdays))
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    for (d in Weekday.values) {
                        val clicked = days.contains(d)
                        FilterChip(
                            modifier = Modifier
                                .padding(
                                    start = 5.dp,
                                    end = 5.dp,
                                ),
                            label = {
                                Text("$d")
                            },
                            leadingIcon = {
                            },
                            selected = clicked,
                            onClick = {
                                days = when  {
                                    clicked -> days - setOf(d)
                                    else -> days + setOf(d)
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}