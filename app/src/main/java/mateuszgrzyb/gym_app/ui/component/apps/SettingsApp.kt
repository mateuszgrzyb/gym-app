package mateuszgrzyb.gym_app.ui.component.apps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.WeightUnit
import mateuszgrzyb.gym_app.db.Settings
import mateuszgrzyb.gym_app.ui.component.AppScaffold
import mateuszgrzyb.gym_app.viewmodels.PermissionsState
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel

@ExperimentalLayoutApi
@ExperimentalMaterial3Api
@Composable
fun SettingsApp(
    settingsViewModel: SettingsViewModel = viewModel(),
    settings: Settings,
    updateSettings: (Settings) -> Unit,
) {
    var newSettings by remember { mutableStateOf(settings) }

    AppScaffold(
        title = stringResource(R.string.settings_tab),
        actions = {
            Button(
                onClick = {
                    updateSettings(newSettings)
                },
            ) {
                Text(stringResource(R.string.save))
            }
        }
    ) { contentModifier ->
        Column(modifier = contentModifier) {
            var expanded by remember { mutableStateOf(false) }
            ListItem(
                modifier = Modifier.padding(5.dp),
                headlineContent = {
                    Text(stringResource(R.string.ads), fontWeight = FontWeight.Bold)
                },
                trailingContent = {
                    Switch(
                        checked = settings.adsEnabled,
                        onCheckedChange = {
                            newSettings = newSettings.copy(adsEnabled = it)
                        }
                    )
                }
            )
            ListItem(
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { expanded = true },
                headlineContent = {
                    Text(stringResource(R.string.weight_unit), fontWeight = FontWeight.Bold)
                },
                trailingContent = {
                    Text("${newSettings.weightUnit}", fontWeight = FontWeight.Bold)
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                DropdownMenuItem(
                    text = { Text("KG") },
                    onClick = {
                        newSettings = newSettings.copy(weightUnit = WeightUnit.KG)
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("LB") },
                    onClick = {
                        newSettings = newSettings.copy(weightUnit = WeightUnit.LB)
                        expanded = false
                    }
                )
            }
        }
    }
}