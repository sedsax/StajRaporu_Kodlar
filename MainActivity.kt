package com.ankauniss.uyumapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ankauniss.uyumapp.ui.theme.UyumAppTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UyumAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                      SayfaGecisleri()
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SayfaGecisleri() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "anasayfa") {
        composable("anasayfa") {
            Anasayfa(navController = navController)
        }
        composable("ogrenciKayit/{kod}", arguments = listOf(
            navArgument("kod"){
                type = NavType.StringType })
        ) {
            val kod = it.arguments?.getString("kod")!!
            OgrenciKayit(navController = navController,kod)
        }
        composable("yoklama/{kod}", arguments = listOf(
            navArgument("kod"){
                type = NavType.StringType })
        ) {
            val kod = it.arguments?.getString("kod")!!
            Yoklama(navController = navController, kod)
        }
        composable("qrmain") {
            QRmain(navController = navController)
        }
        composable("qrmainy") {
            QRmainy(navController = navController)
        }
    }
}

@Composable
fun Anasayfa(navController: NavController) {

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val image: Painter = painterResource(id = R.drawable.ankalogo)

        Image(painter = image,
            contentDescription = "Ankara Üniversitesi Mühendislik Fakültesi Logosu",
            Modifier.size(180.dp)
        )

        BackHandler(enabled = true) {

        }
        Button(onClick = {
            navController.navigate("qrmain")
        }, modifier = Modifier
            .padding(5.dp)
            .size(180.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Öğrenci Kaydet",fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
        }
        Button(onClick = {
            navController.navigate("qrmainy")
        }, modifier = Modifier
            .padding(5.dp)
            .size(180.dp, 50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Yoklama Al", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.White)
        }

    }

}