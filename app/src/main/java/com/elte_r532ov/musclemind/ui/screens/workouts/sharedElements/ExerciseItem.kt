package com.elte_r532ov.musclemind.ui.screens.workouts.sharedElements

import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.elte_r532ov.musclemind.data.workoutsAndExercises.Exercise

@SuppressLint("DiscouragedApi")
@Composable
fun ExerciseItemItem(exercise: Exercise, navigation: NavHostController) {
    val context = LocalContext.current

    val imageResId = context.resources.getIdentifier(exercise.drawablePicName, "drawable", context.packageName)
    if (imageResId != 0) {
        Card(
            modifier =
            Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondary)){
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start){
                    Text(
                        text = exercise.name,
                        modifier = Modifier.padding(start = 16.dp).weight(2f, fill = false),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Image(
                        painter = painterResource(id = getPictureIdByExperienceLevel(exercise.experienceLevel)),
                        contentDescription = exercise.name,
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .align(Alignment.CenterVertically)
                            .padding(end = 16.dp, start = 8.dp)
                    )
                }
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End){
                    Icon(
                        imageVector = Icons.Filled.WatchLater,
                        contentDescription = "Stopwatch",
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = exercise.duration.toString(),
                        modifier = Modifier.weight(2f, fill = false),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End){
                    Icon(
                        imageVector = Icons.Filled.LocalFireDepartment,
                        contentDescription = "Calories",
                        modifier = Modifier.padding(end = 6.dp, start = 12.dp)
                    )
                    Text(
                        text = exercise.caloriesBurnt.toString(),
                        modifier = Modifier.weight(2f, fill = false),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = exercise.name,
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .align(Alignment.CenterVertically)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }
    else{
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = exercise.name,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}