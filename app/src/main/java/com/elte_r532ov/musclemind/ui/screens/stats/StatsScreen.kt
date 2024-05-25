import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.enums.StatsFormat
import com.elte_r532ov.musclemind.data.enums.StatsMode
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.ui.util.BottomNavBar
import com.elte_r532ov.musclemind.ui.screens.stats.StatsViewModel
import com.elte_r532ov.musclemind.ui.util.handleUiEvent

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StatsMainScreen(
    onNavigate: NavHostController,
    viewModel: StatsViewModel = hiltViewModel()
) {
    val snackBarHostState = handleUiEvent(viewModel.uiEvent, onNavigate)

    val scaledKcalStats by viewModel.scaledKcalStats.collectAsState()
    val realKcalStats by viewModel.realKcalStats.collectAsState()

    val scaledTimeStats by viewModel.scaledTimeStats.collectAsState()
    val realTimeStats by viewModel.realTimeStats.collectAsState()

    val selectedMode by viewModel.selectedMode.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()

    Scaffold(
        bottomBar = {
            onNavigate.currentDestination?.route?.let {
                BottomNavBar(it, onNavigate)
            }
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Statistics",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 40.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "You can see your progression by week.",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row( modifier = Modifier
                    .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    ) {
                    Column {
                        Button(
                            onClick = { viewModel.previousWeek() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .height(36.dp)
                        ) {
                            Icon(Icons.Filled.ChevronLeft, "Arrow")
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = "$currentDate s week",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Column {
                        Button(
                            onClick = { viewModel.nextWeek() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier
                                .height(36.dp)
                        ) {
                            Icon(Icons.Filled.ChevronRight, "Arrow")
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SelectionBar(selectedMode) { mode ->
                viewModel.setMode(mode)
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedMode) {
                StatsMode.KCAL -> BarGraph(scaledKcalStats, realKcalStats)
                StatsMode.TIME -> BarGraph(scaledTimeStats, realTimeStats)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Get your stats sent to you by mail. You can choose your preferred format",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.sendStatsByEmail(StatsFormat.PDF) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("PDF")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = { viewModel.sendStatsByEmail(StatsFormat.CSV) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("CSV")
                }
            }
        }
    }
}

@Composable
fun BarGraph(scaledValues: List<Int>, realValues: List<Int>) {
    val barColors = listOf(
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value),
        Color(MaterialTheme.colorScheme.onSurface.value)
    )
    val days = listOf("M", "Tu", "W", "Th", "F", "Sa", "Su")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        scaledValues.forEachIndexed { index, value ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.height(200.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(value.dp)
                        .width(16.dp)
                        .background(barColors[index])
                )
                Text(text = realValues[index].toString())
                Text(text = days[index])
            }
        }
    }
}

@Composable
fun SelectionBar(selectedMode: StatsMode, onModeSelected: (StatsMode) -> Unit) {
    val modes = listOf(StatsMode.KCAL, StatsMode.TIME)
    val titles = listOf("Burnt calories (kcal)", "Time Working Out (min)")

    TabRow(
        selectedTabIndex = modes.indexOf(selectedMode),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        modes.forEachIndexed { index, mode ->
            Tab(
                selected = selectedMode == mode,
                onClick = { onModeSelected(mode) },
                text = { Text(titles[index]) },
                selectedContentColor = if (selectedMode == mode)
                    MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                unselectedContentColor = if (selectedMode == mode)
                    MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            )
        }
    }
}
