package com.elte_r532ov.musclemind.ui.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BottomNavBar(currentNav: String, onNavigate: NavHostController) {
    val items = listOf(
        BottomNavMenu.WORKOUTS,
        BottomNavMenu.STATS,
        BottomNavMenu.CALORIES,
        BottomNavMenu.SETTINGS
    )
    BottomAppBar(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        items.forEach { item ->
            val isSelected = currentNav.contains(item.label, ignoreCase = true)
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.scale(1f)
                    )
                },
                selected = isSelected,
                onClick = { if (!isSelected) { onNavigate.navigate(item.route) } }
            )
        }
    }
}