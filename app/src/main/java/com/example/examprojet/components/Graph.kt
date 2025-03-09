package com.example.examprojet.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun MyLineChartParent(
    values: List<Float>,
    labels: List<String>,
    dateFormat: String = "MM-dd",
    labelValueFormatter: (Float) -> String = { value -> String.format("%.2f", value) },
    xAxisLabel: String = "",
    yAxisLabel: String = "",
    modifier: Modifier = Modifier
) {
    require(values.size == labels.size) { "The number of values must match the number of labels" }

    val inputDateFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM", Locale.getDefault())
    )
    val outputDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())

    val formattedLabels = labels.map { label ->
        var parsedDate: String? = null
        for (inputDateFormat in inputDateFormats) {
            try {
                parsedDate = outputDateFormat.format(inputDateFormat.parse(label)!!)
                break
            } catch (e: ParseException) {
                // Continue to the next date format
            }
        }
        parsedDate ?: label
    }

    val points = values.zip(formattedLabels).mapIndexed { index, (value, label) ->
        LineChartData.Point(value, label)
    }

    Column(modifier = modifier) {
        if (yAxisLabel.isNotEmpty()) {
            Text(yAxisLabel, fontSize = 14.sp, modifier = Modifier.padding(start = 16.dp))
        }
        LineChart(
            linesChartData = listOf(
                LineChartData(
                    points = points,
                    padBy = 0.1f,
                    startAtZero = false,
                    lineDrawer = SolidLineDrawer(1.dp, Color.Blue)
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(16.dp),
            animation = simpleChartAnimation(),
            pointDrawer = FilledCircularPointDrawer(),
            xAxisDrawer = SimpleXAxisDrawer(
                labelTextSize = 12.sp,
                axisLineColor = Color.Transparent,
                labelRatio = 1
            ),
            yAxisDrawer = SimpleYAxisDrawer(
                labelValueFormatter = labelValueFormatter
            ),
            horizontalOffset = 5f
        )
        if (xAxisLabel.isNotEmpty()) {
            Text(
                xAxisLabel,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp)
                    .align(Alignment.End)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestPreview() {
    val sampleValues = listOf(0.8f, 1.2f, 1.8f, 1.6f, 1.1f)
    val sampleLabels = listOf("2024-04", "2024-05", "2024-06", "2024-07", "2024-08")
    MyLineChartParent(
        values = sampleValues,
        labels = sampleLabels,
        xAxisLabel = "Date",
        yAxisLabel = "Value (in billions)",
        dateFormat = "MM-dd",
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .padding(16.dp)
    )
}