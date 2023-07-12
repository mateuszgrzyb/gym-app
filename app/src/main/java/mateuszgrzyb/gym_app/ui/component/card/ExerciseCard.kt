package mateuszgrzyb.gym_app.ui.component.card

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.db.Settings
import mateuszgrzyb.gym_app.ui.component.apps.DetailsState
import mateuszgrzyb.gym_app.ui.component.dialog.DeleteExerciseDialog
import mateuszgrzyb.gym_app.ui.component.dialog.UpdateExerciseDialog
import mateuszgrzyb.gym_app.viewmodels.SettingsViewModel
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel
import kotlin.math.roundToInt

enum class DragState {
    Start,
    Stop,
}

fun between(min: Int, v: Int, max: Int): Int {
    require(min < max)
    return if (v < min) { min } else if (max < v) { max } else { v }
}


enum class DialogState {
    Closed,
    OpenDelete,
    OpenUpdate,
}

const val MAX_OFFSET = 250

@ExperimentalMaterial3Api
@Composable
fun ExerciseCard(
    settingsViewModel: SettingsViewModel = viewModel(),
    workoutId: Long,
    detailsState: DetailsState,
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    exercise: Exercise,
    isDragged: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()

    val settings by settingsViewModel.settings.observeAsState(Settings())

    var clicked by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }

    val dragInteractionFlow = remember { MutableSharedFlow<DragState>() }

    var dialogState by remember { mutableStateOf(DialogState.Closed) }

    var cardModifier = Modifier
        .padding(15.dp)
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = {
                    coroutineScope.launch {
                        dragInteractionFlow.emit(DragState.Start)
                    }
                },
                onDrag = { _, dragAmount ->
                    offsetX += dragAmount.x
                },
                onDragCancel = {
                    coroutineScope.launch {
                        dragInteractionFlow.emit(DragState.Stop)
                    }
                },
                onDragEnd = {
                    coroutineScope.launch {
                        dragInteractionFlow.emit(DragState.Stop)
                    }
                },
            )
        }

    cardModifier = when (detailsState) {
        DetailsState.Display -> cardModifier.clickable { clicked = !clicked }
        DetailsState.Edit -> cardModifier.offset {
            IntOffset(between(-MAX_OFFSET, offsetX.roundToInt(), MAX_OFFSET), 0)
        }
        DetailsState.Reorder -> cardModifier
    }

    LaunchedEffect(dragInteractionFlow, cardModifier) {
        dragInteractionFlow.collect { dragState ->
            when (dragState) {
                DragState.Start -> {}
                DragState.Stop -> {
                    if (detailsState == DetailsState.Edit) {
                        dialogState = when {
                            -MAX_OFFSET < offsetX && offsetX < MAX_OFFSET -> dialogState
                            -MAX_OFFSET >= offsetX -> DialogState.OpenDelete
                            else -> DialogState.OpenUpdate
                        }
                    }
                    offsetX = 0f
                }
            }
        }
    }

    when (dialogState) {
        DialogState.Closed -> {}
        DialogState.OpenDelete -> DeleteExerciseDialog(
            exercise = exercise,
            onConfirm = {
                workoutsViewModel.deleteExercise(it)
                dialogState = DialogState.Closed
            },
            onDismiss = {
                dialogState = DialogState.Closed
            }
        )
        DialogState.OpenUpdate -> UpdateExerciseDialog(
            workoutId = workoutId,
            exercise = exercise,
            onConfirm = {
                workoutsViewModel.updateExercises(it)
                dialogState = DialogState.Closed
            },
            onDismiss = {
                dialogState = DialogState.Closed
            },
        )
    }

    ConstraintLayout {
        val (background, card) = createRefs()
        Box(
            modifier = Modifier
                .padding(15.dp)
                .clip(shape = RoundedCornerShape(13.dp))
                .constrainAs(background) {
                    top.linkTo(card.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(card.bottom)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(MAX_OFFSET.dp)
                        .background(color = Color.Gray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Icon(Icons.Default.Edit, null)
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(MAX_OFFSET.dp)
                        .background(color = Color.Red)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.End,
                    ) {
                        Icon(Icons.Default.Delete, null)
                    }
                }
            }
        }

        Card(
            modifier = cardModifier.constrainAs(card) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            colors = CardDefaults.cardColors(
                containerColor = if (isDragged) {
                    Color.Gray
                } else {
                    MaterialTheme.colorScheme.surface
                },
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Row {
                if (detailsState == DetailsState.Display) {
                    Checkbox(
                        checked = clicked,
                        onCheckedChange = null,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(start = 15.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(15.dp)
                ) {


                    Text(exercise.name, fontWeight = FontWeight.Bold)
                    Text("${stringResource(R.string.sets)}: ${exercise.sets}")
                    Text("${stringResource(R.string.reps)}: ${exercise.reps}")
                    Text("${stringResource(R.string.weight)}: ${exercise.weightValue} ${settings.weightUnit}")
                }
            }
        }
    }
}

