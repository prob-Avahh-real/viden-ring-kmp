// RingScreen.kt — viden-ring interactive ring + stats
package com.videnring.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.videnring.model.*
import kotlin.math.*

@Composable
fun RingScreen() {
    val segments = remember { defaultSegments }
    var selectedIndex by remember { mutableStateOf(-1) }
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        rotation.animateTo(1f, animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ))
    }

    val selected = if (selectedIndex in segments.indices) segments[selectedIndex] else null

    Box(
        Modifier.fillMaxSize().background(Brush.radialGradient(
            listOf(Color(0xFF1A1A3A), Color(0xFF0A0A0F))
        ))
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {

            InteractiveRing(segments, selectedIndex, rotation.value,
                size = 300f, modifier = Modifier,
                onSegmentTap = { selectedIndex = it })

            Spacer(Modifier.height(32.dp))

            if (selected != null) {
                SegmentDetail(selected)
            } else {
                WelcomePanel()
            }
        }
    }
}

@Composable
fun InteractiveRing(
    segments: List<RingSegment>,
    selectedIndex: Int,
    rotation: Float,
    size: Float,
    modifier: Modifier,
    onSegmentTap: (Int) -> Unit
) {
    Canvas(
        modifier = modifier.size(size.dp).pointerInput(segments) {
            detectTapGestures { offset ->
                val center = Offset(size / 2, size / 2)
                val outerRadius = size / 2
                val innerRadius = outerRadius * 0.55f
                val dx = offset.x - center.x
                val dy = offset.y - center.y
                val dist = sqrt(dx * dx + dy * dy)
                if (dist < innerRadius || dist > outerRadius) return@detectTapGestures
                var angle = (atan2(dy.toDouble(), dx.toDouble()) + PI / 2)
                if (angle < 0) angle += 2 * PI
                val deg = (angle * 180 / PI).toFloat()
                val sweepAngle = 360f / segments.size
                for (i in segments.indices) {
                    val start = (rotation * 360 + i * sweepAngle - 90) % 360 + 360
                    if (deg in start % 360..(start % 360 + sweepAngle).coerceAtMost(360f)
                        || deg + 360 in start..start + sweepAngle) {
                        onSegmentTap(i)
                        return@detectTapGestures
                    }
                }
            }
        }
    ) {
        val center = Offset(size / 2, size / 2)
        val outerRadius = size / 2
        val innerRadius = outerRadius * 0.55f
        val sweepAngle = (2 * PI / segments.size).toFloat()

        // Outer glow
        drawCircle(Color(0xFFD4A843).copy(alpha = 0.08f), outerRadius + 40, center)

        // Segments
        for (i in segments.indices) {
            val seg = segments[i]
            val startAngle = (rotation * 2 * PI + i * sweepAngle - (PI / 2).toFloat()).toFloat()
            val isSelected = i == selectedIndex
            val segColor = Color(seg.color)
            val alpha = if (isSelected) 0.9f else 0.5f

            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(center.x + innerRadius * cos(startAngle), center.y + innerRadius * sin(startAngle))
                arcTo(Rect(center = center, radius = innerRadius), Math.toDegrees(startAngle.toDouble()).toFloat(),
                    Math.toDegrees(sweepAngle.toDouble()).toFloat(), false)
                lineTo(center.x + outerRadius * cos(startAngle + sweepAngle),
                    center.y + outerRadius * sin(startAngle + sweepAngle))
                arcTo(Rect(center = center, radius = outerRadius),
                    Math.toDegrees((startAngle + sweepAngle).toDouble()).toFloat(),
                    Math.toDegrees((-sweepAngle).toDouble()).toFloat(), false)
                close()
            }

            drawPath(path, segColor.copy(alpha = alpha))
            drawPath(path, if (isSelected) Color(seg.glowColor).copy(alpha = 0.8f) else segColor.copy(alpha = 0.3f),
                style = Stroke(if (isSelected) 2.5f else 1f))

            if (isSelected) {
                drawPath(path, Color(seg.glowColor).copy(alpha = 0.15f),
                    style = Stroke(12f, cap = StrokeCap.Round))
            }

            // Icon text
            val iconAngle = startAngle + sweepAngle / 2
            val iconRadius = (innerRadius + outerRadius) / 2
            val ix = center.x + iconRadius * cos(iconAngle)
            val iy = center.y + iconRadius * sin(iconAngle)
            // emoji rendering — compose text equivalent
        }

        // Inner circle
        drawCircle(Color(0xFF1E1E3A).copy(alpha = 0.95f), innerRadius, center)
        drawCircle(Color(0xFFD4A843).copy(alpha = 0.4f), innerRadius, center, style = Stroke(2f))

        // Center text
        if (selectedIndex in segments.indices) {
            val sel = segments[selectedIndex]
            // Text rendered via Canvas drawText (platform-specific)
        }
    }
}

@Composable
fun SegmentDetail(seg: RingSegment) {
    Card(
        Modifier.fillMaxWidth().padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF12123A))
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(seg.icon, fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(seg.name, color = Color(seg.glowColor),
                        fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Text("Lv. ${seg.level}", color = Color(0xFFA09888), fontSize = 14.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { seg.progress.toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Color(seg.glowColor),
                trackColor = Color.White.copy(alpha = 0.1f)
            )
            Spacer(Modifier.height(8.dp))
            Text(seg.description, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            if (seg.abilities.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    seg.abilities.forEach { ability ->
                        Surface(color = Color(seg.color).copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.small) {
                            Text(ability, fontSize = 12.sp,
                                color = Color(seg.glowColor),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomePanel() {
    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("⚔", fontSize = 36.sp, color = Color(0xFFD4A843))
        Spacer(Modifier.height(8.dp))
        Text("Tap a segment", color = Color(0xFF6A6378), fontSize = 14.sp)
    }
}
