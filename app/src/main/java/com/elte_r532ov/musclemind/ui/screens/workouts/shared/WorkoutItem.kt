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
import com.elte_r532ov.musclemind.data.enums.ExperienceLevel
import com.elte_r532ov.musclemind.ui.util.Routes

@SuppressLint("DiscouragedApi")
@Composable
fun WorkoutItem(workout: Workout,
                navigation: NavHostController,
                navigateTo: String,
                onWorkoutClick: ((Workout) -> Unit)
) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(
        workout.drawablepicname, "drawable", context.packageName)
    if (imageResId != 0) {
        Card(
            modifier =
            Modifier.fillMaxWidth().
            clickable {
                onWorkoutClick(workout)
                navigation.navigate(navigateTo)
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = workout.name,
                    modifier = Modifier
                        .weight(1f, fill = false)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = workout.name,
                    modifier = Modifier
                        .padding(16.dp)
                        .weight(1f),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text(
                        text = "Difficulty: ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Image(
                        painter = painterResource(id = getPictureIdByExperienceLevel(
                            ExperienceLevel.valueOf(workout.experiencelevel.toString().uppercase()))),
                        contentDescription = workout.name,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
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