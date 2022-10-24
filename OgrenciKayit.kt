package com.ankauniss.uyumapp

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OgrenciKayit(navController: NavController, gelenQRcode: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        BackHandler(enabled = true) {

        }
        val db = Firebase.firestore
        var name by remember { mutableStateOf("") }
        var surname by remember { mutableStateOf("") }
        var no by remember { mutableStateOf("") }
        val dateTime = LocalDateTime.now()

        val bolumListe = mutableListOf("Bölüm","Bilgisayar (Eng)","Bilgisayar Müh", "Biyomedikal Müh","Kimya Müh","Fizik Müh",
            "Elektrik Elektronik Müh","Yapay Zeka Müh","Yazılım Müh","Gıda Müh",
            "Jeoloji Müh","Jeofizik Müh","İnşaat Müh","Enerji Sistemleri Müh")

        var acilisKontrol by remember { mutableStateOf(false)}
        var secilenIndeks by remember { mutableStateOf(0)}

        TextField(
            value = gelenQRcode,
            onValueChange = { },
            modifier =   Modifier.padding(30.dp),
            enabled = false)
        TextField(
            value = name,
            onValueChange = { name = it },
            modifier =   Modifier.padding(5.dp),
            placeholder = { Text(text = "İsim") })
        TextField(
            value = surname,
            onValueChange = { surname = it },
            modifier = Modifier.padding(5.dp),
            placeholder = { Text(text = "Soyisim") })
        TextField(
            value = no,
            onValueChange = { no = it },
            modifier = Modifier.padding(5.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = { Text(text = "No") })
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
                    }}
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
        val user = hashMapOf<String, Any>(
            "ad" to name,
            "soyad" to surname,
            "no" to no,
            "bolüm" to bolumListe[secilenIndeks],
            "qrcode" to gelenQRcode,
            "time" to (dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT))).toString(),
        )
        val context = LocalContext.current
        Button(onClick = {
            if(name.isNotEmpty() && surname.isNotEmpty() && no.isNotEmpty() && secilenIndeks != 0) {
                db.collection(bolumListe[secilenIndeks]).add(user).addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(context,"Öğrenci Kaydedildi",Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context,exception.localizedMessage,Toast.LENGTH_SHORT).show()
                }
                navController.navigate("anasayfa")
            }else{
                Toast.makeText(context, "Bilgileri eksiksiz girin!", Toast.LENGTH_SHORT).show()
            }
        }
        ) {
            Text(text = "Kaydet")
        }
    }

}