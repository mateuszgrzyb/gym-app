package mateuszgrzyb.gym_app.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import mateuszgrzyb.gym_app.activities.WorkoutsActivity
import mateuszgrzyb.gym_app.activities.SettingsActivity
import mateuszgrzyb.gym_app.toBoolean
import mateuszgrzyb.gym_app.toDrawerValue
import mateuszgrzyb.gym_app.viewmodels.ContextViewModel

@ExperimentalMaterial3Api
@Composable
fun AppScaffold(
    contextViewModel: ContextViewModel = viewModel(),
    title: String,
    fap: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable (Modifier) -> Unit,
) {
    var drawerVisible by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(0.dp, 5.dp),
                ) {
                    NavigationDrawerItem(
                        modifier = Modifier.padding(10.dp, 5.dp),
                        label = {
                            Text("Main")
                        },
                        selected = contextViewModel.currentClassName() == WorkoutsActivity::class.qualifiedName,
                        onClick = { contextViewModel.switchActivity(WorkoutsActivity::class.java) }
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.padding(10.dp, 5.dp),
                        label = {
                            Text("Settings")
                        },
                        selected = contextViewModel.currentClassName() == SettingsActivity::class.qualifiedName,
                        onClick = { contextViewModel.switchActivity(SettingsActivity::class.java) }
                    )
                }
            }
        },
        drawerState = DrawerState(
            initialValue = drawerVisible.toDrawerValue(),
            confirmStateChange = { value ->
                drawerVisible = value.toBoolean()
                true
            }
        )
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconToggleButton(
                            checked = drawerVisible,
                            onCheckedChange = { drawerVisible = true }
                        ) {
                            Icon(
                                Icons.Default.List,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = actions,
                    title = { Text(title) },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(MaterialTheme.colorScheme.primary),
                )
            },
            floatingActionButton = fap,
        ) { contentPadding ->
            content(Modifier.padding(contentPadding))
        }
    }

}

