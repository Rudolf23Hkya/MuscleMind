package com.elte_r532ov.musclemind.ui.screens.workouts.sharedElements

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Workout

@Composable
fun WorkoutItem(workout: Workout, navigate: NavHostController) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(workout.drawablePicName, "drawable", context.packageName)
    if (imageResId != 0) {
        Card(
            modifier =
            Modifier.fillMaxWidth().
            clickable {
                //TODO - Navigate To The Workout s in Detail Page
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = workout.name,
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth(0.75f)
                )
                Text(
                    text = workout.name,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    else{
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = workout.name,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}