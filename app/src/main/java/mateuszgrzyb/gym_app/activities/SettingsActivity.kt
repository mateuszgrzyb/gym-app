package mateuszgrzyb.gym_app.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import mateuszgrzyb.gym_app.ui.components.AppScaffold
import mateuszgrzyb.gym_app.ui.theme.GymappTheme

@ExperimentalMaterial3Api
@AndroidEntryPoint
class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GymappTheme(darkTheme = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScaffold(
                        title = "Settings",
                    ) { contentModifier ->
                        Column(modifier = contentModifier) {
                            Text("ala ma kota")
                        }
                    }
                }
            }
        }
    }
}