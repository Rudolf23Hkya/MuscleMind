package com.elte_r532ov.musclemind
import com.elte_r532ov.musclemind.ui.screens.login.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight



val myFontFamily = FontFamily(
    Font(R.font.inter_black,FontWeight.Black),
    Font(R.font.inter_medium,FontWeight.Medium),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_bold,FontWeight.Bold),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}



