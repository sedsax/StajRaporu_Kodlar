package com.ankauniss.uyumapp

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Yoklama(navController: NavHostController, gelenQRcode: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        val db = Firebase.firestore
        val context = LocalContext.current

        BackHandler(enabled = true) {

        }
        val bolumListe = mutableListOf("Bölüm","Bilgisayar (Eng)","Bilgisayar Müh", "Biyomedikal Müh","Kimya Müh","Fizik Müh",
            "Elektrik Elektronik Müh","Yapay Zeka Müh","Yazılım Müh","Gıda Müh",
            "Jeoloji Müh","Jeofizik Müh","İnşaat Müh","Enerji Sistemleri Müh")

        var acilisKontrol by remember { mutableStateOf(false) }
        var secilenIndeks by remember { mutableStateOf(0) }

        TextField(
            value = gelenQRcode,
            onValueChange = { },
            modifier =   Modifier.padding(30.dp),
            enabled = false)

        Box{
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable(onClick = {
                        acilisKontrol = true // satıra tıklanınca dropdown menu açılması tetikleriz
                    })
            ) {

                TextField(
                    value = bolumListe[secilenIndeks],
                    onValueChange = { bolumListe[secilenIndeks] = it },
                    modifier = Modifier.padding(5.dp),
                    enabled = false,
                    trailingIcon = { IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Mühendislik Bölümlerinin Listesi" )
                    }
                    }
                )
            }

            DropdownMenu(expanded = acilisKontrol, //Dropdown açılışını tetikleme
                onDismissRequest = { acilisKontrol = false}, //Dropdown Menu kapatılır
            ) {
                bolumListe.forEachIndexed {index, bolum ->
                    //Liste içeriğini döngü ile alıyoruz ve liste içeriği kadar DropdownMenuItem oluşturuyoruz
                    DropdownMenuItem(onClick = { //Her bir item a tıklanma
                        secilenIndeks = index
                        acilisKontrol = false
                    }) {
                        Text(text = bolum)
                    }
                }

            }
        }
        val dateTime = LocalDateTime.now()
        val netTime = (dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))).toString()
        val ek = hashMapOf<String, Any>(
            "zaman" to netTime,
            "QR KOD" to gelenQRcode
        )
        Button(onClick = {
            if(bolumListe[secilenIndeks].isNotEmpty() && secilenIndeks != 0) {
                db.collection(bolumListe[secilenIndeks]).addSnapshotListener { snapshot, error ->
                    if(error!=null) {
                        Toast.makeText(context, "hata!", Toast.LENGTH_SHORT).show()
                    }else {
                        if (snapshot != null) {
                            //name = qr // bu if içine giriyoruz demek ki snapshot null değil
                            if(!snapshot.isEmpty) { // bölümleri temsil ediyor
                                // name = qr // bu if içine giremiyoruz demek ki snapshot boş değil if(snapshot.isEmpty) böyle yapmışım :/
                                val documents = snapshot.documents
                                for(document in documents){
                                    if(document.get("qrcode") != null){
                                        val qr = document.get("qrcode") as String
                                        if(qr == gelenQRcode){
                                            db.collection(bolumListe[secilenIndeks]).document(document.id).collection("yoklama").add(ek)
                                            Toast.makeText(context, "YOKLAMA ALINDI!", Toast.LENGTH_SHORT).show()
                                            break
                                        }else{
                                            Toast.makeText(context, "YOKLAMA ALINAMADI!", Toast.LENGTH_SHORT).show()
                                        }
                                    }else{
                                        Toast.makeText(context, "QR kod null", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }else{
                                Toast.makeText(context, "QR kod bilgisi yok", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            Toast.makeText(context, "Böyle bir kayıt yok", Toast.LENGTH_SHORT).show()
                        }
                    }
                    navController.navigate("anasayfa")
                    //navController.popBackStack()
                }
            }else{
                Toast.makeText(context, "Bölüm bilgisi girin!", Toast.LENGTH_SHORT).show()
            }
        }
        ) {
            Text(text = "Yoklama Al")
        }
    }
}