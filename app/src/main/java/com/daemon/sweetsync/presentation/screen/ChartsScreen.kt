package com.daemon.sweetsync.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.daemon.sweetsync.data.model.BloodSugarReading
import com.daemon.sweetsync.presentation.viewmodel.BloodSugarUiState
import com.daemon.sweetsync.presentation.viewmodel.BloodSugarViewModel
import com.daemon.sweetsync.utils.DateTimeUtils
import com.github.mikephil.charting.charts.LineChart
import java.util.Date
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    onNavigateBack: () -> Unit,
    bloodSugarViewModel: BloodSugarViewModel
) {
    val uiState by bloodSugarViewModel.uiState.collectAsState()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = false

    // Add this to prevent flickering during transitions
    var isInitialized by remember { mutableStateOf(false) }
    LaunchedEffect(uiState.readings) {
        if (uiState.readings.isNotEmpty() && !isInitialized) {
            isInitialized = true
        }
    }

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = AppColors.BackgroundPrimary, // or any dark color you're using
            darkIcons = useDarkIcons
        )
    }

    Scaffold(
        containerColor = AppColors.BackgroundPrimary,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Sugar Level Analytics \uD83D\uDCCA",
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextOnSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppColors.TextOnSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.BackgroundSurface,
                    titleContentColor = AppColors.TextOnSurface,
                    navigationIconContentColor = AppColors.TextOnSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            if (uiState.isLoading) {
                LoadingSection()
            } else if (uiState.readings.isEmpty()) {
                EmptyStateSection()
            } else {

                Spacer(modifier = Modifier.height(16.dp))

                // Week Chart Section
                ChartSection(
                    title = "Last 7 Records",
                    subtitle = "Recent readings histogram",
                    icon = Icons.Default.DateRange,
                    uiState = uiState,
                    period = ChartPeriod.WEEK
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Month Chart Section
                ChartSection(
                    title = "Monthly Analysis",
                    subtitle = "4-week progression",
                    icon = Icons.Default.DateRange,
                    uiState = uiState,
                    period = ChartPeriod.MONTH
                )

                Spacer(modifier = Modifier.height(16.dp))

                ChartSection(
                    title = "All Records",
                    subtitle = "Complete history plot",
                    icon = Icons.Default.DateRange,
                    uiState = uiState,
                    period = ChartPeriod.YEAR
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            uiState.errorMessage?.let { error ->
                ErrorSection(error)
            }
        }
    }
}

@Composable
fun LoadingSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CircularProgressIndicator(
                    color = AppColors.PrimaryBlue,
                    strokeWidth = 3.dp
                )
                Text(
                    text = "Loading your health data...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.TextSecondary
                )
            }
        }
    }
}

@Composable
fun EmptyStateSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterVertically as Alignment.Horizontal,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = AppColors.TextSecondary
            )
            Text(
                text = "No Data Available",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
            Text(
                text = "Start tracking your blood sugar levels to see beautiful charts and insights here!",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.TextSecondary
            )
        }
    }
}

@Composable
fun ErrorSection(error: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AppColors.ErrorRed.copy(alpha = 0.2f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = null,
                tint = AppColors.ErrorRed
            )
            Text(
                text = error,
                color = AppColors.ErrorRed,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ChartSection(
    title: String,
    subtitle: String,
    icon: ImageVector,
    uiState: BloodSugarUiState,
    period: ChartPeriod
) {
    Spacer(modifier = Modifier.padding(top = 16.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Column {
            // Section Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                AppColors.PrimaryBlue.copy(alpha = 0.3f),
                                AppColors.SecondaryTeal.copy(alpha = 0.2f)
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = AppColors.PrimaryBlue
                    )
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.TextPrimary
                        )
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.TextSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(top = 5.dp))

            // Chart
            if (period == ChartPeriod.WEEK) {
                BloodSugarHistogramChart(
                    uiState = uiState,
                    readings = uiState.readings,
                    period = period,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp)
                )
            } else {
                BloodSugarLineChart(
                    uiState = uiState,
                    readings = uiState.readings,
                    period = period,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp)
                )
            }

            // Statistics
            StatisticsCard(
                readings = uiState.readings,
                period = period,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun BloodSugarHistogramChart(
    uiState: BloodSugarUiState,
    readings: List<BloodSugarReading>,
    period: ChartPeriod,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val primaryColor = AppColors.PrimaryBlue.toArgb()
    val surfaceColor = AppColors.BackgroundCard.toArgb()
    val onSurfaceColor = AppColors.TextPrimary.toArgb()
    val normalColor = AppColors.SugarNormal.toArgb()
    val lowColor = AppColors.SugarLow.toArgb()
    val highColor = AppColors.SugarHigh.toArgb()
    var gridColor = AppColors.Grey300.toArgb()

    val filteredReadings = remember(readings, period) {
        filterReadingsByPeriod(readings, period)
    }

    val chartData = remember(filteredReadings, period) {
        processDataForChart(filteredReadings, period)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundSecondary
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                BarChart(ctx).apply {
                    description.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(true)
                    setPinchZoom(true)
                    setDrawGridBackground(false)
                    setBackgroundColor(surfaceColor)
                    setDrawBarShadow(false)
                    setDrawValueAboveBar(true)
                    setBackgroundColor(AppColors.BackgroundSecondary.toArgb())

                    // X-axis configuration
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        granularity = 1f
                        labelCount = chartData.labels.size
                        textColor = onSurfaceColor
                        textSize = 11f
                        setLabelRotationAngle(-45f)
                        valueFormatter = IndexAxisValueFormatter(chartData.labels)
                        axisLineColor = onSurfaceColor
                        axisLineWidth = 1.5f
                    }

                    // Left Y-axis configuration
                    axisLeft.apply {
                        setDrawGridLines(true)
                        gridColor = gridColor
                        gridLineWidth = 1f
                        textColor = onSurfaceColor
                        textSize = 11f
                        axisLineColor = onSurfaceColor
                        axisLineWidth = 1.5f

                        val validValues = chartData.values.filter { !it.isNaN() && it > 0f }
                        val maxValue = validValues.maxOrNull() ?: 300f
                        val minValue = validValues.minOrNull() ?: 70f
                        axisMinimum = 0f
                        axisMaximum = maxOf(300f, maxValue + 50f)

                        // Reference lines
                        addLimitLine(com.github.mikephil.charting.components.LimitLine(70f, "Low").apply {
                            lineColor = lowColor
                            lineWidth = 2f
                            enableDashedLine(10f, 10f, 0f)
                        })
                        addLimitLine(com.github.mikephil.charting.components.LimitLine(180f, "High").apply {
                            lineColor = highColor
                            lineWidth = 2f
                            enableDashedLine(10f, 10f, 0f)
                        })
                    }

                    axisRight.isEnabled = false

                    legend.apply {
                        isEnabled = true
                        textColor = onSurfaceColor
                        textSize = 12f
                    }

                    setExtraOffsets(15f, 15f, 15f, 15f)
                }
            },
            update = { chart ->
                if (chart.data != null && chart.data.dataSetCount > 0) {
                    return@AndroidView
                }
                try {
                    if (chartData.values.isEmpty() || chartData.values.all { it.isNaN() || it <= 0f }) {
                        chart.clear()
                        chart.invalidate()
                        return@AndroidView
                    }

                    if (chartData.values.isNotEmpty()) {
                        val entries = chartData.values.mapIndexedNotNull { index, value ->
                            if (!value.isNaN() && value > 0f) {
                                BarEntry(index.toFloat(), value)
                            } else null
                        }

                        if (entries.isNotEmpty()) {
                            val dataSet = BarDataSet(entries, "Blood Sugar (mg/dL)").apply {
                                // Color bars based on glucose level
                                colors = entries.map { entry ->
                                    when {
                                        entry.y < 70f -> lowColor
                                        entry.y > 180f -> highColor
                                        else -> normalColor
                                    }
                                }
                                valueTextSize = 10f
                                valueTextColor = onSurfaceColor
                                setDrawValues(true)
                            }

                            val barData = BarData(dataSet).apply {
                                barWidth = 0.8f
                            }

                            chart.data = barData
                            chart.notifyDataSetChanged()
                            chart.invalidate()
                            chart.animateY(1200)
                        }
                    } else {
                        chart.clear()
                        chart.invalidate()
                    }
                } catch (e: Exception) {
                    println("Histogram chart update error: ${e.message}")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun BloodSugarLineChart(
    uiState: BloodSugarUiState,
    readings: List<BloodSugarReading>,
    period: ChartPeriod,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val primaryColor = AppColors.PrimaryBlue.toArgb()
    val surfaceColor = AppColors.BackgroundCard.toArgb()
    val onSurfaceColor = AppColors.TextPrimary.toArgb()
    val normalColor = AppColors.SugarNormal.toArgb()
    val lowColor = AppColors.SugarLow.toArgb()
    val highColor = AppColors.SugarHigh.toArgb()
    var gridColor = AppColors.Grey300.toArgb()

    val filteredReadings = remember(readings, period) {
        filterReadingsByPeriod(readings, period)
    }

    val chartData = remember(filteredReadings, period) {
        if (filteredReadings.isEmpty()) {
            ChartData(emptyList(), emptyList())
        } else {
            processDataForChart(filteredReadings, period)
        }
    }

// Add this state to track if chart is already populated
    var isChartPopulated by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundSecondary
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                LineChart(ctx).apply {
                    // Enhanced chart styling for dark theme
                    description.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(true)
                    setPinchZoom(true)
                    setDrawGridBackground(false)
                    setBackgroundColor(AppColors.BackgroundSecondary.toArgb())


                    setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                            // Value selection handled automatically by the chart
                        }
                        override fun onNothingSelected() {
                            // Deselection handled automatically
                        }
                    })
                    // Enable highlighting on tap/zoom
                    setHighlightPerTapEnabled(true)
                    setDrawMarkers(true)

                    setOnChartGestureListener(object : com.github.mikephil.charting.listener.OnChartGestureListener {
                        override fun onChartGestureStart(me: android.view.MotionEvent?, lastPerformedGesture: com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture?) {}
                        override fun onChartGestureEnd(me: android.view.MotionEvent?, lastPerformedGesture: com.github.mikephil.charting.listener.ChartTouchListener.ChartGesture?) {
                            // Update value display based on zoom level
                            data?.dataSets?.forEach { dataSet ->
                                if (dataSet is LineDataSet) {
                                    dataSet.setDrawValues(scaleX > 1.5f || scaleY > 1.5f)
                                }
                            }
                            invalidate()
                        }
                        override fun onChartLongPressed(me: android.view.MotionEvent?) {}
                        override fun onChartDoubleTapped(me: android.view.MotionEvent?) {}
                        override fun onChartSingleTapped(me: android.view.MotionEvent?) {}
                        override fun onChartFling(me1: android.view.MotionEvent?, me2: android.view.MotionEvent?, velocityX: Float, velocityY: Float) {}
                        override fun onChartScale(me: android.view.MotionEvent?, scaleX: Float, scaleY: Float) {}
                        override fun onChartTranslate(me: android.view.MotionEvent?, dX: Float, dY: Float) {}
                    })


                    // X-axis configuration with dark theme styling
                    xAxis.apply {
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(true)
                        gridColor = gridColor
                        gridLineWidth = 1f
                        granularity = 1f
                        labelCount = 7
                        textColor = onSurfaceColor
                        textSize = 11f
                        setLabelRotationAngle(-30f)
                        valueFormatter = IndexAxisValueFormatter(chartData.labels)
                        setAvoidFirstLastClipping(true)
                        axisLineColor = onSurfaceColor
                        axisLineWidth = 1.5f
                    }

                    // Enhanced Left Y-axis configuration for dark theme
                    axisLeft.apply {
                        setDrawGridLines(true)
                        gridColor = gridColor
                        gridLineWidth = 1f
                        textColor = onSurfaceColor
                        textSize = 11f
                        axisLineColor = onSurfaceColor
                        axisLineWidth = 1.5f

                        // Dynamic Y-axis range based on data
                        val validValues = chartData.values.filter { !it.isNaN() && it > 0f }
                        val maxValue = validValues.maxOrNull() ?: 300f
                        val minValue = validValues.minOrNull() ?: 70f
                        axisMinimum = if (minValue > 0f) maxOf(0f, minValue - 20f) else 0f
                        axisMaximum = if (maxValue > 0f) maxOf(300f, maxValue + 50f) else 300f

                        // Styled reference lines
                        addLimitLine(com.github.mikephil.charting.components.LimitLine(70f, "Low").apply {
                            lineColor = lowColor
                            lineWidth = 1f
                            enableDashedLine(10f, 10f, 0f)
                            labelPosition = com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.RIGHT_TOP
                            textColor = lowColor
                            textSize = 9f
                        })
                        addLimitLine(com.github.mikephil.charting.components.LimitLine(180f, "High").apply {
                            lineColor = highColor
                            lineWidth = 1f
                            enableDashedLine(10f, 10f, 0f)
                            labelPosition = com.github.mikephil.charting.components.LimitLine.LimitLabelPosition.RIGHT_TOP
                            textColor = highColor
                            textSize = 9f
                        })
                    }

                    axisRight.isEnabled = false

                    // Enhanced legend configuration for dark theme
                    legend.apply {
                        isEnabled = true
                        textColor = onSurfaceColor
                        textSize = 12f
                        form = com.github.mikephil.charting.components.Legend.LegendForm.LINE
                        formLineWidth = 3f
                        xEntrySpace = 10f
                        yEntrySpace = 5f
                    }

                    // Enhanced padding
                    setExtraOffsets(15f, 15f, 15f, 15f)
                }
            },
            update = { chart ->
                if (chart.data != null && chart.data.dataSetCount > 0) {
                    return@AndroidView
                }
                try {
                    if (chartData.values.isEmpty() || chartData.values.all { it.isNaN() || it <= 0f }) {
                        chart.clear()
                        chart.invalidate()
                        return@AndroidView
                    }

                    println("Updating chart with ${chartData.values.size} data points")

                    if (chartData.values.isNotEmpty()) {
                        // Filter out NaN values and create entries
                        val entries = chartData.values.mapIndexedNotNull { index, value ->
                            if (!value.isNaN() && value > 0f) {
                                Entry(index.toFloat(), value)
                            } else {
                                null
                            }
                        }

                        println("Valid entries after filtering: ${entries.size}")

                        val dataSets = mutableListOf<ILineDataSet>()

                        if (entries.isNotEmpty()) {
                            // Enhanced main blood sugar line
                            val dataSet = LineDataSet(entries, "Blood Sugar (mg/dL)").apply {
                                color = primaryColor
                                setCircleColor(primaryColor)
                                circleHoleColor = surfaceColor
                                lineWidth = 3f
                                circleRadius = 4f
                                setDrawCircleHole(true)
                                circleHoleRadius = 2f
                                valueTextSize = 10f
                                valueTextColor = onSurfaceColor
                                setDrawValues(chart.scaleX > 1.5f || chart.scaleY > 1.5f)

                                mode = LineDataSet.Mode.CUBIC_BEZIER
                                cubicIntensity = 0.15f

                                // Enhanced fill with gradient effect
                                setDrawFilled(true)
                                fillAlpha = 60
                                fillColor = primaryColor

                                // Custom value formatter
                                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                                    override fun getFormattedValue(value: Float): String {
                                        return "${value.toInt()}"
                                    }
                                }
                            }
                            dataSets.add(dataSet)

                            // Enhanced normal range indicator - only if we have data points
                            val normalRangeEntries = entries.map { entry ->
                                Entry(entry.x, 125f)
                            }

                            val normalRangeDataSet = LineDataSet(normalRangeEntries, "Target Range").apply {
                                color = normalColor
                                lineWidth = 1f
                                setDrawCircles(false)
                                setDrawValues(false)
                                enableDashedLine(8f, 8f, 0f)
                                setDrawFilled(false)
                            }
                            dataSets.add(normalRangeDataSet)
                        }

                        val lineData = LineData(dataSets)
                        chart.data = lineData
                        chart.notifyDataSetChanged()
                        chart.invalidate()
                        chart.animateX(1200)
                    } else {
                        println("No valid data to display")
                        chart.clear()
                        chart.invalidate()
                    }

                } catch (e: Exception) {
                    println("Chart update error: ${e.message}")
                    e.printStackTrace()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(8.dp)
        )
    }
}

@Composable
fun StatisticsCard(
    readings: List<BloodSugarReading>,
    period: ChartPeriod,
    modifier: Modifier = Modifier
) {
    val filteredReadings = remember(readings, period) {
        filterReadingsByPeriod(readings, period)
    }

    val stats = remember(filteredReadings) {
        calculateStatistics(filteredReadings)
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = AppColors.BackgroundSecondary
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "${period.displayName} Statistics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Warning for All Records if no recent readings
            if (period == ChartPeriod.YEAR && stats.daysSinceLastReading > 7) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.ErrorRed.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = AppColors.ErrorRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "⚠️ Need to test Blood Sugar - Last reading was ${stats.daysSinceLastReading} days ago",
                            style = MaterialTheme.typography.bodyMedium,
                            color = AppColors.ErrorRed,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Main statistics row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Average",
                    value = "${stats.average.toInt()}",
                    unit = "mg/dL",
                    color = AppColors.PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )

                StatItem(
                    label = "In Range",
                    value = "${stats.inRangePercentage.toInt()}",
                    unit = "%",
                    color = if (stats.inRangePercentage >= 70)
                        AppColors.SugarNormal else AppColors.ErrorRed,
                    modifier = Modifier.weight(1f)
                )

                StatItem(
                    label = "Readings",
                    value = stats.count.toString(),
                    unit = "total",
                    color = AppColors.SecondaryTeal,
                    modifier = Modifier.weight(1f)
                )
            }

            // Max/Min values row - only for All Records
            if (period == ChartPeriod.YEAR && stats.count > 0) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItemWithDate(
                        label = "Highest",
                        value = "${stats.max.toInt()}",
                        unit = "mg/dL",
                        date = stats.maxDate,
                        color = if (stats.max > 180) AppColors.SugarHigh else AppColors.PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )

                    StatItemWithDate(
                        label = "Lowest",
                        value = "${stats.min.toInt()}",
                        unit = "mg/dL",
                        date = stats.minDate,
                        color = if (stats.min < 70) AppColors.SugarLow else AppColors.PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun StatItemWithDate(
    label: String,
    value: String,
    unit: String = "",
    date: String = "",
    color: androidx.compose.ui.graphics.Color = AppColors.PrimaryBlue,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextHint
            )
        }
        if (date.isNotEmpty()) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextHint,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    unit: String = "",
    color: androidx.compose.ui.graphics.Color = AppColors.PrimaryBlue,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = AppColors.TextSecondary,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                style = MaterialTheme.typography.bodySmall,
                color = AppColors.TextHint
            )
        }
    }
}


enum class ChartPeriod(val displayName: String, val days: Long) {
    WEEK("Last 7 Records", 7),
    MONTH("Last 30 Days", 30),
    YEAR("All Records", 365)
}

data class ChartData(
    val labels: List<String>,
    val values: List<Float>
)

data class Statistics(
    val average: Double,
    val max: Double,
    val min: Double,
    val count: Int,
    val inRangePercentage: Double,
    val lowCount: Int,
    val normalCount: Int,
    val highCount: Int,
    val maxDate: String = "",
    val minDate: String = "",
    val daysSinceLastReading: Long = 0
)

fun parseDateTime(timestamp: Long): LocalDateTime {
    return try {
        val date = Date(timestamp)
        val calendar = java.util.Calendar.getInstance()
        calendar.time = date
        LocalDateTime.of(
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH) + 1,
            calendar.get(java.util.Calendar.DAY_OF_MONTH),
            calendar.get(java.util.Calendar.HOUR_OF_DAY),
            calendar.get(java.util.Calendar.MINUTE),
            calendar.get(java.util.Calendar.SECOND)
        )
    } catch (e: Exception) {
        println("Failed to parse timestamp: $timestamp")
        LocalDateTime.MIN
    }
}

// Then use this function in filterReadingsByPeriod:
fun filterReadingsByPeriod(readings: List<BloodSugarReading>, period: ChartPeriod): List<BloodSugarReading> {
    return when (period) {
        ChartPeriod.WEEK -> {
            readings.sortedByDescending { reading ->
                parseDateTime(reading.timestamp)
            }.take(7)
        }
        else -> {
            val cutoffDate = LocalDateTime.now().minus(period.days, ChronoUnit.DAYS)
            readings.filter { reading ->
                val readingDate = parseDateTime(reading.timestamp)
                readingDate.isAfter(cutoffDate) && readingDate != LocalDateTime.MIN
            }.sortedByDescending {
                parseDateTime(it.timestamp)
            }
        }
    }
}

fun processDataForChart(readings: List<BloodSugarReading>, period: ChartPeriod): ChartData {
    println("Processing chart data for period: $period with ${readings.size} readings")

    if (readings.isEmpty()) {
        return ChartData(emptyList(), emptyList())
    }

    when (period) {
        ChartPeriod.WEEK -> {
            // For WEEK, the readings passed here are already filtered to last 7 records
            // Just process them directly (they're already in descending order from filter)
            val last7 = readings.reversed() // Reverse to show oldest to newest

            val labels = last7.mapIndexed { idx, reading ->
                try {
                    val dt = parseDateTime(reading.timestamp)
                    "Record ${idx + 1}\n${dt.format(DateTimeFormatter.ofPattern("MMM dd"))}"
                } catch (e: Exception) {
                    "Record ${idx + 1}"
                }
            }
            val values = last7.map { it.glucose_level.toFloat() }

            println("Last 7 records histogram - Labels: $labels, Values: $values")
            return ChartData(labels, values)
        }

        ChartPeriod.MONTH -> {
            // Last 30 days up to latest entry, grouped by day
            val mostRecent = readings.maxByOrNull { it.timestamp }
            val mostRecentDate = mostRecent?.let { parseDateTime(it.timestamp) } ?: LocalDateTime.now()
            val endDate = mostRecentDate.toLocalDate()
            val startDate = endDate.minusDays(29)

            val daysToShow = (0..29).map { startDate.plusDays(it.toLong()) }
            val labels = daysToShow.map { it.format(DateTimeFormatter.ofPattern("MMM dd")) }
            val values = daysToShow.map { day ->
                val dayReadings = readings.filter { reading ->
                    try {
                        val readingDate = parseDateTime(reading.timestamp).toLocalDate()
                        readingDate.isEqual(day)
                    } catch (e: Exception) {
                        false
                    }
                }
                if (dayReadings.isNotEmpty()) {
                    dayReadings.map { it.glucose_level }.average().toFloat()
                } else {
                    Float.NaN
                }
            }
            println("Last 30 days - Labels: $labels, Values: $values")
            return ChartData(labels, values)
        }

        ChartPeriod.YEAR -> {
            // All entries chronologically ordered
            val sortedReadings = readings.sortedBy { it.timestamp }

            val labels = sortedReadings.map { reading ->
                try {
                    val dt = parseDateTime(reading.timestamp)
                    dt.format(DateTimeFormatter.ofPattern("MMM dd"))
                } catch (e: Exception) {
                    "Entry"
                }
            }
            val values = sortedReadings.map { it.glucose_level.toFloat() }

            println("All time entries - Labels: ${labels.size} entries, Values: ${values.size} values")
            return ChartData(labels, values)
        }
    }
}



fun calculateStatistics(readings: List<BloodSugarReading>): Statistics {
    if (readings.isEmpty()) {
        return Statistics(0.0, 0.0, 0.0, 0, 0.0, 0, 0, 0)
    }

    val values = readings.map { it.glucose_level }
    val average = values.average()
    val max = values.maxOrNull() ?: 0.0
    val min = values.minOrNull() ?: 0.0
    val count = readings.size

    val lowCount = readings.count { it.glucose_level < 70.0 }
    val normalCount = readings.count { it.glucose_level in 70.0..180.0 }
    val highCount = readings.count { it.glucose_level > 180.0 }

    val inRangePercentage = if (count > 0) (normalCount.toDouble() / count) * 100 else 0.0

    // Find max and min with their dates
    val maxReading = readings.maxByOrNull { it.glucose_level }
    val minReading = readings.minByOrNull { it.glucose_level }

    val maxDate = maxReading?.let { reading ->
        try {
            val dt = parseDateTime(reading.timestamp)
            if (dt != LocalDateTime.MIN) dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) else ""
        } catch (e: Exception) {
            ""
        }
    } ?: ""

    val minDate = minReading?.let { reading ->
        try {
            val dt = parseDateTime(reading.timestamp)
            if (dt != LocalDateTime.MIN) dt.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")) else ""
        } catch (e: Exception) {
            ""
        }
    } ?: ""

    // Calculate days since last reading
    val daysSinceLastReading = if (readings.isNotEmpty()) {
        val mostRecentReading = readings.maxByOrNull { reading ->
            parseDateTime(reading.timestamp)
        }
        mostRecentReading?.let { reading ->
            val lastReadingDate = parseDateTime(reading.timestamp)
            if (lastReadingDate != LocalDateTime.MIN) {
                ChronoUnit.DAYS.between(lastReadingDate, LocalDateTime.now())
            } else 0L
        } ?: 0L
    } else 0L

    return Statistics(average, max, min, count, inRangePercentage, lowCount, normalCount, highCount, maxDate, minDate, daysSinceLastReading)
}