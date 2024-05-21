package com.elte_r532ov.musclemind.ui.screens.workouts.shared

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.api.responses.Workout

@SuppressLint("DiscouragedApi")
@Composable
fun WorkoutItem(workout: Workout, navigation: NavHostController) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(workout.drawablepicname, "drawable", context.packageName)
    if (imageResId != 0) {
        Card(
            modifier =
            Modifier.fillMaxWidth().
            clickable {
                val workoutId = workout.workoutid
                navigation.navigate("workouts_in_detail/$workoutId")
            }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = workout.name,
                    modifier = Modifier
                        .weight(1f, fill = false) // To the image to occupy required space but not fill horizontally
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Absolute.Left){
                Text(
                    text = workout.name,
                    modifier = Modifier.padding(16.dp).weight(2f, fill = false),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
    else{
        Card(
            modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = workout.name,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}