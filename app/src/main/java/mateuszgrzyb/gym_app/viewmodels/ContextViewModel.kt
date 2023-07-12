package mateuszgrzyb.gym_app.viewmodels

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.textsearch.createIndex
import java.io.InputStream
import java.util.stream.Collectors

class ContextViewModel(
    application: Application
) : AndroidViewModel(application) {
    fun switchActivity(activityClass: Class<*>) {
        val context: Context = getApplication()
        val intent = Intent(context, activityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun currentClassName(): String? {
        val c = getApplication() as Context
        val am = c.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo =  am.getRunningTasks(1)
        return taskInfo.get(0).topActivity?.className
    }

    private fun getFile(): List<String> {
        val c = getApplication() as Context
        val ins: InputStream = c.resources.openRawResource(R.raw.suggestions)
        return ins.bufferedReader().use {
            it.lines().collect(Collectors.toList())
        }
    }

    val fileContents: List<String> = getFile()

    val index = createIndex(getFile())
}