package it.livasodv.app
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import it.livasodv.app.data.SupabaseSync
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.livasodv.app.feature.*
import it.livasodv.app.ui.theme.LivasTheme

class MainActivity:ComponentActivity(){override fun onCreate(b:Bundle?){super.onCreate(b);setContent{LivasTheme{App()}}}}
data class Tool(val name:String,val icon:ImageVector)
@Composable fun App(){
 var loggedIn by remember{mutableStateOf(false)}
 var screen by remember{mutableStateOf("home")}
 var syncError by remember{mutableStateOf<String?>(null)}
 LaunchedEffect(loggedIn){if(loggedIn){try{SupabaseSync().refreshCore()}catch(e:Exception){syncError=e.message}}}
 if(!loggedIn){LoginScreen{loggedIn=true};return}
 if(screen!="home"){
  androidx.activity.compose.BackHandler { screen="home" }
  Column(Modifier.fillMaxSize()){
   Surface(tonalElevation=2.dp){
    Row(Modifier.fillMaxWidth().padding(8.dp),verticalAlignment=Alignment.CenterVertically){
     IconButton({screen="home"}){Icon(Icons.Default.ArrowBack,"Home")}
     Text("LÌ.V.A.S.",fontWeight=FontWeight.Bold)
     Spacer(Modifier.weight(1f))
     syncError?.let{Text("Offline",color=Color(0xFFFF9800))}
    }
   }
   Box(Modifier.weight(1f)){when(screen){
    "soci"->MembersScreen();"mezzi"->VehiclesScreen();"magazzino"->WarehouseScreen();"presidi"->PresidiScreen()
    "turni"->ShiftsScreen();"servizi"->ServicesScreen();"comunicazioni"->CommunicationsScreen()
    "cittadini"->CitizenRequestsScreen();"sc"->CivilServiceScreen();"registro"->AuditScreen()
    "operativo"->MissionsScreen();"ricerca"->GlobalSearchScreen();"backup"->BackupScreen()
    "scadenze"->ExpiryScreen();"notifiche"->NotificationCenterScreen();"emergenze"->EmergencyScreen()
    "ps"->MonitorPSScreen();"pc"->CivilProtectionScreen();"game"->RescueRunScreen()
    else->Home{screen=it}
   }}
  }
 } else Home{screen=it}
}
@Composable fun Home(open:(String)->Unit){
 val tools=listOf("soci" to Tool("SOCI",Icons.Default.Group),"comunicazioni" to Tool("COMUNICAZIONI",Icons.Default.Campaign),"mezzi" to Tool("MEZZI",Icons.Default.DirectionsCar),"turni" to Tool("TURNI",Icons.Default.CalendarMonth),"servizi" to Tool("SERVIZI",Icons.Default.MedicalServices),"magazzino" to Tool("MAGAZZINO",Icons.Default.Inventory2),"presidi" to Tool("PRESIDI",Icons.Default.HealthAndSafety),"sc" to Tool("SERVIZIO CIVILE",Icons.Default.School),"cittadini" to Tool("CITTADINI",Icons.Default.Person),"emergenze" to Tool("EMERGENZE",Icons.Default.Emergency),"ps" to Tool("MONITOR PS 118",Icons.Default.MonitorHeart),"pc" to Tool("PROTEZIONE CIVILE",Icons.Default.Cloud),"game" to Tool("PASSATEMPO",Icons.Default.SportsEsports),"ricerca" to Tool("RICERCA",Icons.Default.Search),"backup" to Tool("BACKUP",Icons.Default.Backup),"scadenze" to Tool("SCADENZE",Icons.Default.EventBusy),"notifiche" to Tool("NOTIFICHE",Icons.Default.Notifications),"registro" to Tool("REGISTRO",Icons.Default.History),"operativo" to Tool("OPERATIVO",Icons.Default.Emergency))
 Scaffold(containerColor=Color(0xFF070707)){p->LazyVerticalGrid(GridCells.Fixed(2),Modifier.fillMaxSize().padding(p),contentPadding=PaddingValues(16.dp),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalArrangement=Arrangement.spacedBy(12.dp)){
  item(span={GridItemSpan(2)}){Column(Modifier.fillMaxWidth().padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally){Icon(Icons.Default.VolunteerActivism,null,Modifier.size(110.dp),Color(0xFFFF3434));Text("LÌ.V.A.S.",style=MaterialTheme.typography.headlineLarge,fontWeight=FontWeight.Black);Text("GONNOSFANADIGA",color=Color.Red)}}
  items(tools){(route,t)->Card(onClick={open(route)},modifier=Modifier.height(125.dp),border=androidx.compose.foundation.BorderStroke(1.dp,Color.Red),colors=CardDefaults.cardColors(Color(0xFF111111))){Column(Modifier.fillMaxSize(),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.Center){Icon(t.icon,null,Modifier.size(42.dp),Color.Red);Spacer(Modifier.height(8.dp));Text(t.name,fontWeight=FontWeight.Bold)}}}
 }}
}
