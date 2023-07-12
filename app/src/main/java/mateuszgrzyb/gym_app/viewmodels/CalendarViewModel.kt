package mateuszgrzyb.gym_app.viewmodels

import android.app.Application
import android.content.Context
import android.provider.CalendarContract
import androidx.lifecycle.AndroidViewModel
import mateuszgrzyb.gym_app.data.CalendarEventData

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    fun createEventForWorkout(data: CalendarEventData) {
        val context: Context = getApplication()

        val contentResolver = context.contentResolver
        val values = data.toContentValues()

        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }
}