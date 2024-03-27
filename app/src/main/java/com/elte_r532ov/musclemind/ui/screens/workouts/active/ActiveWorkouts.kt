package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.util.BottomNavItem
import com.elte_r532ov.musclemind.util.BottomNavMenu
import com.elte_r532ov.musclemind.util.UiEvent
import androidx.compose.runtime.livedata.observeAsState
import com.elte_r532ov.musclemind.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActiveWorkouts(
    onNavigate: NavHostController,
    viewModel: ActiveWorkoutsViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val userName by viewModel.userNameLiveData.observeAsState("")

    val workout_image_1 by viewModel.userNameLiveData.observeAsState("")
    val workout_image_2 by viewModel.userNameLiveData.observeAsState("")

    val items = listOf(
       BottomNavMenu.WORKOUTS,
        BottomNavMenu.STATS,
        BottomNavMenu.CALORIES,
        BottomNavMenu.SETTINGS
    )
    //SnackBar
    var snackBarMessage by remember { mutableStateOf<String?>(null) }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate.navigate(event.route)
                is UiEvent.ShowSnackbar -> snackBarMessage = event.message
                is UiEvent.ErrorOccured -> snackBarMessage = event.errMsg
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(items = items, navController = navController, onItemClick = {
                navController.navigate(it.route)
            })
        },
        snackbarHost ={ SnackbarHost(snackBarHostState) }

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE1D5E7)), // Use a gradient instead for the background
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 54.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Hello $userName",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
            WorkoutList()
        }
    }
}
//TODO - Lazy list upgrade
@Composable
fun WorkoutList() {
    Column(modifier = Modifier.padding(16.dp)) {
        WorkoutCard("Workout Name1", R.drawable.warmup)
        Spacer(modifier = Modifier.height(8.dp))
        WorkoutCard("Workout Name2", R.drawable.own_body_weight)
    }
}

@Composable
fun BottomNavBar(items: List<BottomNavItem>, navController: NavHostController, onItemClick: (BottomNavItem) -> Unit) {
    BottomAppBar {
        items.forEach { item ->
            val isSelected = item.route == navController.currentDestination?.route
            NavigationBarItem(
                icon = { Icon(painter = painterResource(id = item.icon), contentDescription = item.label, modifier = Modifier.size(20.dp)) },
                label = { Text(item.label) },
                selected = isSelected,
                onClick = { onItemClick(item) }
            )
        }
    }
}
@Composable
fun WorkoutCard(name: String, drawable: Int) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = drawable),
                contentDescription = name,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth(0.75f)
            )
            Text(
                text = name,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
