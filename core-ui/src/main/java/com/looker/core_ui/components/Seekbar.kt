package com.looker.core_ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.looker.core_ui.utils.lerp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

private const val waveWidth = 50F
private const val waveHeight = 10F

internal val ThumbRadius = 8.dp
internal val TrackHeight = 4.dp
private val SliderHeight = 48.dp
private val SliderMinWidth = 144.dp

val DefaultSliderConstraints =
	Modifier
		.widthIn(min = SliderMinWidth)
		.heightIn(max = SliderHeight)

@Composable
fun WaveySeekbar(
	value: Float,
	onValueChange: (Float) -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
	steps: Int = 0,
	onValueChangeFinished: (() -> Unit)? = null,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	colors: SliderColors = SliderDefaults.colors()
) {
	val onValueChangeState = rememberUpdatedState(onValueChange)
	BoxWithConstraints(
		modifier
			.requiredSizeIn(minWidth = ThumbRadius * 2, minHeight = ThumbRadius * 2)
			.sliderSemantics(value, enabled, onValueChange, valueRange, steps)
			.focusable(enabled, interactionSource)
	) {
		val widthPx = constraints.maxWidth.toFloat()
		val maxPx: Float
		val minPx: Float

		with(LocalDensity.current) {
			maxPx = max(widthPx - ThumbRadius.toPx(), 0f)
			minPx = min(ThumbRadius.toPx(), maxPx)
		}

		fun scaleToUserValue(offset: Float) =
			scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

		fun scaleToOffset(userValue: Float) =
			scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

		val rawOffset = remember { mutableStateOf(scaleToOffset(value)) }
		val pressOffset = remember { mutableStateOf(0f) }

		val draggableState = remember(minPx, maxPx, valueRange) {
			SliderDraggableState {
				rawOffset.value = (rawOffset.value + it + pressOffset.value)
				pressOffset.value = 0f
				val offsetInTrack = rawOffset.value.coerceIn(minPx, maxPx)
				onValueChangeState.value.invoke(scaleToUserValue(offsetInTrack))
			}
		}

		CorrectValueSideEffect(::scaleToOffset, valueRange, minPx..maxPx, rawOffset, value)

		val gestureEndAction = rememberUpdatedState { _: Float ->
			if (!draggableState.isDragging) {
				onValueChangeFinished?.invoke()
			}
		}

		val press = Modifier.sliderTapModifier(
			draggableState = draggableState,
			interactionSource = interactionSource,
			maxPx = widthPx,
			rawOffset = rawOffset,
			gestureEndAction = gestureEndAction,
			pressOffset = pressOffset,
			enabled = enabled
		)

		val drag = Modifier.draggable(
			orientation = Orientation.Horizontal,
			reverseDirection = false,
			enabled = enabled,
			interactionSource = interactionSource,
			onDragStopped = { velocity -> gestureEndAction.value.invoke(velocity) },
			startDragImmediately = draggableState.isDragging,
			state = draggableState
		)

		val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
		val fraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)
		CustomSliderImpl(
			enabled,
			fraction,
			colors,
			maxPx - minPx,
			interactionSource,
			modifier = press.then(drag)
		)
	}
}

@Composable
private fun CorrectValueSideEffect(
	scaleToOffset: (Float) -> Float,
	valueRange: ClosedFloatingPointRange<Float>,
	trackRange: ClosedFloatingPointRange<Float>,
	valueState: MutableState<Float>,
	value: Float
) {
	SideEffect {
		val error = (valueRange.endInclusive - valueRange.start) / 1000
		val newOffset = scaleToOffset(value)
		if (abs(newOffset - valueState.value) > error) {
			if (valueState.value in trackRange) {
				valueState.value = newOffset
			}
		}
	}
}

@Composable
private fun CustomSliderImpl(
	enabled: Boolean,
	positionFraction: Float,
	colors: SliderColors,
	width: Float,
	interactionSource: MutableInteractionSource,
	modifier: Modifier
) {
	Box(modifier.then(DefaultSliderConstraints)) {
		val trackStrokeWidth: Float
		val thumbPx: Float
		val widthDp: Dp
		with(LocalDensity.current) {
			trackStrokeWidth = (TrackHeight / 2).toPx()
			thumbPx = ThumbRadius.toPx()
			widthDp = width.toDp()
		}

		val thumbSize = ThumbRadius * 2
		val offset = widthDp * positionFraction

		CustomTrack(
			Modifier.fillMaxSize(),
			colors,
			enabled,
			positionFraction,
			thumbPx,
			trackStrokeWidth
		)
		SliderThumb(Modifier, offset, interactionSource, colors, enabled, thumbSize)
	}
}

@Composable
private fun CustomTrack(
	modifier: Modifier,
	colors: SliderColors,
	enabled: Boolean,
	positionFractionEnd: Float,
	thumbPx: Float,
	trackStrokeWidth: Float
) {
	val inactiveTrackColor = colors.trackColor(enabled, active = false)
	val activeTrackColor = colors.trackColor(enabled, active = true)
//	val deltaXAnim = rememberInfiniteTransition()
//	val dx by deltaXAnim.animateFloat(
//		initialValue = 0f,
//		targetValue = waveWidth,
//		animationSpec = infiniteRepeatable(
//			animation = tween(1000, easing = LinearEasing)
//		)
//	)

	Canvas(modifier) {
		val sliderLeft = Offset(thumbPx, center.y)
		val sliderRight = Offset(size.width - thumbPx, center.y)
		val sliderValueEnd = Offset(
			sliderLeft.x + (sliderRight.x - sliderLeft.x) * positionFractionEnd,
			center.y
		)
		drawLine(
			inactiveTrackColor.value,
			sliderValueEnd,
			sliderRight,
			trackStrokeWidth,
			StrokeCap.Round
		)

		val sliderValueStart = Offset(
			sliderLeft.x + (sliderRight.x - sliderLeft.x) * 0f,
			center.y
		)
		val points = mutableListOf<Offset>()

		for (x in (sliderValueStart.x.toInt())..(sliderValueEnd.x.toInt())) {
			val offsetY =
				((sin(x * (2f * PI / waveWidth)) * (waveHeight / (2)) + (waveHeight / 2)).toFloat()
						+ (sliderValueStart.y - (waveHeight / 2)))
			val offsetX = x.toFloat()
			points.add(Offset(offsetX, offsetY))
		}
		drawPoints(
			points = points,
			strokeWidth = trackStrokeWidth,
			pointMode = PointMode.Points,
			color = activeTrackColor.value,
			cap = StrokeCap.Round
		)
	}
}

@Composable
private fun BoxScope.SliderThumb(
	modifier: Modifier,
	offset: Dp,
	interactionSource: MutableInteractionSource,
	colors: SliderColors,
	enabled: Boolean,
	thumbSize: Dp
) {
	Box(
		Modifier
			.padding(start = offset)
			.align(Alignment.CenterStart)
	) {
		val interactions = remember { mutableStateListOf<Interaction>() }
		LaunchedEffect(interactionSource) {
			interactionSource.interactions.collect { interaction ->
				when (interaction) {
					is PressInteraction.Press -> interactions.add(interaction)
					is PressInteraction.Release -> interactions.remove(interaction.press)
					is PressInteraction.Cancel -> interactions.remove(interaction.press)
					is DragInteraction.Start -> interactions.add(interaction)
					is DragInteraction.Stop -> interactions.remove(interaction.start)
					is DragInteraction.Cancel -> interactions.remove(interaction.start)
				}
			}
		}

		Spacer(
			modifier
				.size(thumbSize, thumbSize)
				.indication(
					interactionSource = interactionSource,
					indication = rememberRipple(bounded = false, radius = 24.dp)
				)
				.hoverable(interactionSource = interactionSource)
				.background(colors.thumbColor(enabled).value, CircleShape)
		)
	}
}

private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
	lerp(a2, b2, calcFraction(a1, b1, x1))

private fun calcFraction(a: Float, b: Float, pos: Float) =
	(if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private fun Modifier.sliderTapModifier(
	draggableState: DraggableState,
	interactionSource: MutableInteractionSource,
	maxPx: Float,
	rawOffset: State<Float>,
	gestureEndAction: State<(Float) -> Unit>,
	pressOffset: MutableState<Float>,
	enabled: Boolean
) = composed(
	factory = {
		if (enabled) {
			val scope = rememberCoroutineScope()
			pointerInput(draggableState, interactionSource, maxPx) {
				detectTapGestures(
					onPress = { pos ->
						val to = pos.x
						pressOffset.value = to - rawOffset.value
						try {
							awaitRelease()
						} catch (_: GestureCancellationException) {
							pressOffset.value = 0f
						}
					},
					onTap = {
						scope.launch {
							draggableState.drag(MutatePriority.UserInput) {
								dragBy(0f)
							}
							gestureEndAction.value.invoke(0f)
						}
					}
				)
			}
		} else {
			this
		}
	},
	inspectorInfo = debugInspectorInfo {
		name = "sliderTapModifier"
		properties["draggableState"] = draggableState
		properties["interactionSource"] = interactionSource
		properties["maxPx"] = maxPx
		properties["rawOffset"] = rawOffset
		properties["gestureEndAction"] = gestureEndAction
		properties["pressOffset"] = pressOffset
		properties["enabled"] = enabled
	})

private fun Modifier.sliderSemantics(
	value: Float,
	enabled: Boolean,
	onValueChange: (Float) -> Unit,
	valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
	steps: Int = 0
): Modifier {
	val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
	return semantics {
		if (!enabled) disabled()
		setProgress(
			action = { targetValue ->
				val newValue = targetValue.coerceIn(valueRange.start, valueRange.endInclusive)
				if (newValue == coerced) {
					false
				} else {
					onValueChange(newValue)
					true
				}
			}
		)
	}.progressSemantics(value, valueRange, steps)
}

private class SliderDraggableState(
	val onDelta: (Float) -> Unit
) : DraggableState {

	var isDragging by mutableStateOf(false)
		private set

	private val dragScope: DragScope = object : DragScope {
		override fun dragBy(pixels: Float): Unit = onDelta(pixels)
	}

	private val scrollMutex = MutatorMutex()

	override suspend fun drag(
		dragPriority: MutatePriority,
		block: suspend DragScope.() -> Unit
	): Unit = coroutineScope {
		isDragging = true
		scrollMutex.mutateWith(dragScope, dragPriority, block)
		isDragging = false
	}

	override fun dispatchRawDelta(delta: Float) {
		return onDelta(delta)
	}
}