package mateuszgrzyb.gym_app.ui.components

import android.util.Log
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import mateuszgrzyb.gym_app.R
import mateuszgrzyb.gym_app.db.Exercise
import mateuszgrzyb.gym_app.viewmodels.WorkoutsViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

enum class DragState {
    Start,
    Stop,
}

fun between(min: Int, v: Int, max: Int): Int {
    require(min < max)
    return if (v < min) { min } else if (max < v) { max } else { v }
}

@Composable
fun ExerciseCard(
    editable: Boolean,
    workoutsViewModel: WorkoutsViewModel = viewModel(),
    exercise: Exercise,
) {
    val coroutineScope = rememberCoroutineScope()

    var clicked by remember { mutableStateOf(false) }
    var offsetX by remember { mutableStateOf(0f) }

    val dragInteractionFlow = remember { MutableSharedFlow<DragState>() }

    var openDeleteExerciseDialog by remember { mutableStateOf(false) }

    val maxOffset = 250

    var modifier = Modifier
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

    if (!editable) {
        modifier = modifier.clickable { clicked = !clicked }
    } else {
        modifier = modifier
            .offset { IntOffset(between(-maxOffset, offsetX.roundToInt(), maxOffset), 0) }
    }

    LaunchedEffect(dragInteractionFlow, editable) {
        dragInteractionFlow.collect { dragState ->
            when (dragState) {
                DragState.Start -> {}
                DragState.Stop -> {
                    if (editable && abs(offsetX) >= maxOffset) {
                        openDeleteExerciseDialog = true
                    }
                    offsetX = 0f
                }
            }
        }
    }

    if (openDeleteExerciseDialog) {
        DeleteExerciseDialog(
            exercise = exercise,
            onConfirm = {
                coroutineScope.launch {
                    workoutsViewModel.deleteExercise(it)
                }
                openDeleteExerciseDialog = false
            },
            onDismiss = {
                openDeleteExerciseDialog = false
            }
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
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(maxOffset.dp)
                    .background(color = Color.Red)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                }
            }
        }

        Card(
            modifier = modifier.constrainAs(card) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
        ) {
            Row {
                if (!editable) {
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
                    Text("${stringResource(R.string.weight)}: ${exercise.weightValue} ${exercise.weightUnit}")
                }
            }
        }
    }
}

