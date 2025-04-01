package com.myapps.custombargraph

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.round

@Composable
fun BarGraphWithBarClick(
    graphBarData: List<Float>,
    xAxisScaleData: List<Int>,
    barData_: List<Int>,
    height: Dp,
    roundType: BarType,
    barWidth: Dp,
    barColor: Color,
    barArrangement: Arrangement.Horizontal
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val xAxisScaleHeight = 40.dp
    val yAxisScaleSpacing = 100f
    val yAxisTextWidth = 100.dp

    val barShape = when (roundType) {
        BarType.CIRCULAR_TYPE -> CircleShape
        BarType.TOP_CURVED -> RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)
    }

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val yCoordinates = mutableListOf<Float>()
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    var tooltipData by remember { mutableStateOf<Pair<Int, Float>?>(null) }

    Box(modifier = Modifier.fillMaxWidth()) {

        // Y-axis scale and horizontal lines
        Canvas(
            modifier = Modifier
                .padding(top = xAxisScaleHeight, end = 3.dp)
                .height(height)
                .fillMaxWidth()
        ) {
            val maxValue = barData_.maxOrNull() ?: 0
            val yAxisStep = maxValue / 3f

            (0..3).forEach { i ->
                val yValue = size.height - yAxisScaleSpacing - i * size.height / 3f
                drawContext.canvas.nativeCanvas.drawText(
                    "${round(yAxisStep * i)}",
                    30f,
                    yValue,
                    textPaint
                )
                yCoordinates.add(yValue)
            }

            (1..3).forEach {
                drawLine(
                    start = Offset(x = yAxisScaleSpacing + 30f, y = yCoordinates[it]),
                    end = Offset(x = size.width, y = yCoordinates[it]),
                    color = Color.Gray,
                    strokeWidth = 2f,
                    pathEffect = pathEffect
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(screenWidth - yAxisTextWidth)
                .height(height + xAxisScaleHeight),
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                modifier = Modifier.width(screenWidth - yAxisTextWidth),
                horizontalArrangement = barArrangement,
                verticalAlignment = Alignment.Bottom
            ) {
                graphBarData.forEachIndexed { index, value ->
                    var animatedHeight by remember { mutableStateOf(0f) }
                    LaunchedEffect(Unit) { animatedHeight = value }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(barShape)
                                .width(barWidth)
                                .height(height)
                                .background(Color.Transparent)
                                .clickable {
                                    tooltipData = Pair(xAxisScaleData[index], value)
                                },
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(animatedHeight)
                                    .clip(barShape)
                                    .background(barColor)
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(10.dp)
                                .background(Color.Gray)
                        )

                        Text(
                            text = xAxisScaleData[index].toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 3.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Gray)
            )
        }

        tooltipData?.let { (xValue, yValue) ->
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .background(Color.Black, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "X: $xValue, Y: ${String.format("%.2f", yValue)}",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }

            LaunchedEffect(tooltipData) {
                delay(2000)
                tooltipData = null
            }
        }
    }
}

















