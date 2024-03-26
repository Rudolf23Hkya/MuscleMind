package com.elte_r532ov.musclemind.ui.screens.workouts.active

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.elte_r532ov.musclemind.myFontFamily
import com.elte_r532ov.musclemind.util.BottomNavItem
import com.elte_r532ov.musclemind.util.BottomNavMenu

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ActiveWorkouts() {
    val navController = rememberNavController()
    val items = listOf(
       BottomNavMenu.WORKOUTS,
        BottomNavMenu.STATS,
        BottomNavMenu.CALORIES,
        BottomNavMenu.SETTINGS
    )

    Scaffold(
        bottomBar = {
            BottomNavBar(items = items, navController = navController, onItemClick = {
                navController.navigate(it.route)
            })
        }
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
                    "Hello ",
                    fontSize = 32.sp,
                    fontFamily = myFontFamily,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
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
