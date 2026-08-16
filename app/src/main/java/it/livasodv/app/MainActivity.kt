package it.livasodv.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import it.livasodv.app.data.SupabaseSync
import it.livasodv.app.feature.*
import it.livasodv.app.ui.theme.LivasTheme

class MainActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState:Bundle?){
        super.onCreate(savedInstanceState)
        setContent{LivasTheme{LivasAndroidApp()}}
    }
}

data class HomeTool(val route:String,val title:String,val subtitle:String,val icon:ImageVector)

@Composable
fun LivasAndroidApp(){
    var loggedIn by remember{mutableStateOf(false)}
    var screen by remember{mutableStateOf("home")}
    var syncError by remember{mutableStateOf<String?>(null)}

    LaunchedEffect(loggedIn){
        if(loggedIn){
            try{SupabaseSync().refreshCore();syncError=null}
            catch(e:Exception){syncError=e.message}
        }
    }

    if(!loggedIn){LoginScreen{loggedIn=true};return}

    if(screen!="home"){
        BackHandler{screen="home"}
        Column(Modifier.fillMaxSize().background(Color(0xFF050505))){
            Surface(color=Color(0xFF111111)){
                Row(Modifier.fillMaxWidth().padding(8.dp),verticalAlignment=Alignment.CenterVertically){
                    IconButton(onClick={screen="home"}){Icon(Icons.Default.ArrowBack,"Home")}
                    Text("LÌ.V.A.S.",fontWeight=FontWeight.Black)
                    Spacer(Modifier.weight(1f))
                    if(syncError!=null)Text("OFFLINE",color=Color(0xFFFFA000),fontSize=11.sp)
                }
            }
            Box(Modifier.weight(1f)){
                when(screen){
                    "soci"->MembersScreen()
                    "mezzi"->VehiclesScreen()
                    "magazzino"->WarehouseScreen()
                    "presidi"->PresidiScreen()
                    "turni"->ShiftsScreen()
                    "servizi"->ServicesScreen()
                    "comunicazioni"->CommunicationsScreen()
                    "cittadini"->CitizenRequestsScreen()
                    "sc"->CivilServiceScreen()
                    "registro"->AuditScreen()
                    "operativo"->MissionsScreen()
                    "ricerca"->GlobalSearchScreen()
                    "backup"->BackupScreen()
                    "scadenze"->ExpiryScreen()
                    "notifiche"->NotificationCenterScreen()
                    "emergenze"->EmergencyScreen()
                    "ps"->MonitorPSScreen()
                    "pc"->CivilProtectionScreen()
                    "game"->RescueRunScreen()
                }
            }
        }
    } else HomeScreen(syncError!=null,{screen=it}){loggedIn=false}
}

@Composable
fun HomeScreen(offline:Boolean,onOpen:(String)->Unit,onLogout:()->Unit){
    val tools=listOf(
        HomeTool("turni","I MIEI TURNI","Prossimi servizi",Icons.Default.CalendarMonth),
        HomeTool("servizi","SERVIZI SOCIALI","Richieste",Icons.Default.MedicalServices),
        HomeTool("magazzino","MAGAZZINO","Materiale e DPI",Icons.Default.Inventory2),
        HomeTool("mezzi","MEZZI","Gestione e scadenze",Icons.Default.DirectionsCar),
        HomeTool("soci","SOCI","Elenco e ruoli",Icons.Default.Groups),
        HomeTool("comunicazioni","COMUNICAZIONI","Avvisi e news",Icons.Default.Campaign),
        HomeTool("presidi","PRESIDI","Ausili e dotazioni",Icons.Default.HealthAndSafety),
        HomeTool("sc","SERVIZIO CIVILE","Turni e corsi",Icons.Default.School),
        HomeTool("cittadini","CITTADINI","Richieste",Icons.Default.Person),
        HomeTool("emergenze","EMERGENZE","FAQ e numeri utili",Icons.Default.Phone),
        HomeTool("ps","MONITOR PS 118","Pronto Soccorso",Icons.Default.MonitorHeart),
        HomeTool("pc","PROTEZIONE CIVILE","Allerte e incendi",Icons.Default.Cloud),
        HomeTool("game","PASSATEMPO","Rescue Run",Icons.Default.SportsEsports),
        HomeTool("ricerca","RICERCA","Trova subito",Icons.Default.Search),
        HomeTool("backup","BACKUP","Esporta e ripristina",Icons.Default.Backup),
        HomeTool("scadenze","SCADENZE","Mezzi, DPI e corsi",Icons.Default.EventBusy),
        HomeTool("registro","REGISTRO","Attività",Icons.Default.History),
        HomeTool("operativo","OPERATIVO","Missioni",Icons.Default.Emergency)
    )

    Scaffold(
        containerColor=Color(0xFF050505),
        bottomBar={
            NavigationBar(containerColor=Color(0xFF111111)){
                NavigationBarItem(true,{}, {Icon(Icons.Default.Home,null)},label={Text("Home")})
                NavigationBarItem(false,{onOpen("turni")},{Icon(Icons.Default.CalendarMonth,null)},label={Text("Turni")})
                NavigationBarItem(false,{onOpen("servizi")},{Icon(Icons.Default.MedicalServices,null)},label={Text("Servizi")})
                NavigationBarItem(false,{onOpen("mezzi")},{Icon(Icons.Default.DirectionsCar,null)},label={Text("Mezzi")})
                NavigationBarItem(false,onLogout,{Icon(Icons.Default.Logout,null)},label={Text("Esci")})
            }
        }
    ){padding->
        LazyVerticalGrid(
            columns=GridCells.Fixed(2),
            modifier=Modifier.fillMaxSize().padding(padding).background(
                Brush.verticalGradient(listOf(Color(0xFF030303),Color(0xFF160707),Color(0xFF030303)))
            ),
            contentPadding=PaddingValues(16.dp),
            horizontalArrangement=Arrangement.spacedBy(12.dp),
            verticalArrangement=Arrangement.spacedBy(12.dp)
        ){
            item(span={GridItemSpan(2)}){
                Column(Modifier.fillMaxWidth().padding(vertical=12.dp),horizontalAlignment=Alignment.CenterHorizontally){
                    androidx.compose.foundation.Image(
                        painter=painterResource(R.drawable.livas_logo),
                        contentDescription="Logo Lì.v.a.s.",
                        modifier=Modifier.size(235.dp),
                        contentScale=ContentScale.Fit
                    )
                    Text("LÌ.V.A.S. O.D.V.",fontSize=27.sp,fontWeight=FontWeight.Black,color=Color.White)
                    Text("GONNOSFANADIGA",color=Color(0xFFFF3B30),fontWeight=FontWeight.Bold,letterSpacing=1.5.sp)
                    Text("Insieme per aiutare, sempre.",color=Color(0xFFAAAAAA),fontSize=13.sp)
                    if(offline){Spacer(Modifier.height(8.dp));Text("OFFLINE · dati locali disponibili",color=Color(0xFFFFA000),fontSize=11.sp)}
                }
            }
            items(tools){tool->
                Card(
                    onClick={onOpen(tool.route)},
                    modifier=Modifier.fillMaxWidth().height(140.dp),
                    colors=CardDefaults.cardColors(containerColor=Color(0xEE111111)),
                    border=BorderStroke(1.dp,Color(0xFFFF3030)),
                    shape=RoundedCornerShape(22.dp)
                ){
                    Column(Modifier.fillMaxSize().padding(12.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){
                        Box(Modifier.size(54.dp).background(Color(0xFFB51616),CircleShape),contentAlignment=Alignment.Center){
                            Icon(tool.icon,null,tint=Color.White,modifier=Modifier.size(29.dp))
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(tool.title,color=Color.White,fontWeight=FontWeight.Black,fontSize=13.sp,textAlign=TextAlign.Center)
                        Text(tool.subtitle,color=Color(0xFF8E8E8E),fontSize=11.sp,textAlign=TextAlign.Center)
                    }
                }
            }
            item(span={GridItemSpan(2)}){
                Text("ANPAS  •  PROTEZIONE CIVILE  •  REGIONE SARDEGNA  •  LÌ.V.A.S.\nInsieme per aiutare, sempre.",
                    modifier=Modifier.fillMaxWidth().padding(18.dp),color=Color(0xFF999999),fontSize=10.sp,textAlign=TextAlign.Center)
            }
        }
    }
}
