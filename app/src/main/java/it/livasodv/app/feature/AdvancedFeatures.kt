package it.livasodv.app.feature
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.livasodv.app.data.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant

@Composable fun GlobalSearchScreen(){
 val r=AppGraph.repository; var q by remember{mutableStateOf("")}
 val members by r.members.collectAsState();val vehicles by r.vehicles.collectAsState();val wh by r.warehouse.collectAsState();val pr by r.presidi.collectAsState()
 val rows=remember(q,members,vehicles,wh,pr){if(q.isBlank()) emptyList() else buildList{
  addAll(members.filter{("${it.firstName} ${it.lastName} ${it.role}").contains(q,true)}.map{"Socio" to "${it.firstName} ${it.lastName}"})
  addAll(vehicles.filter{("${it.name} ${it.licensePlate}").contains(q,true)}.map{"Mezzo" to it.name})
  addAll(wh.filter{it.name.contains(q,true)}.map{"Magazzino" to it.name})
  addAll(pr.filter{it.name.contains(q,true)}.map{"Presidio" to it.name})
 }}
 Column(Modifier.fillMaxSize().padding(16.dp)){Text("Ricerca globale",style=MaterialTheme.typography.headlineSmall);OutlinedTextField(q,{q=it},label={Text("Cerca nell'app")},modifier=Modifier.fillMaxWidth());LazyColumn{items(rows){ListItem(headlineContent={Text(it.second)},supportingContent={Text(it.first)})}}}
}
object BackupCodec {
 private val json=Json{prettyPrint=true;ignoreUnknownKeys=true}
 fun encode(b:FullBackup)=json.encodeToString(b)
 fun decode(s:String)=json.decodeFromString<FullBackup>(s)
}
@Composable fun BackupScreen(){
 val context=LocalContext.current
 var status by remember{mutableStateOf<String?>(null)}
 val create=rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")){uri->
  if(uri!=null) try{
   val r=AppGraph.repository
   val b=FullBackup(java.time.Instant.now().toString(),"android-1.2",
    r.members.value,r.vehicles.value,r.warehouse.value,r.presidi.value,r.shifts.value,r.services.value,
    r.communications.value,r.citizenRequests.value,r.civilVolunteers.value,r.civilRequests.value,r.audit.value,r.missions.value)
   context.contentResolver.openOutputStream(uri)?.use{it.write(BackupCodec.encode(b).toByteArray())}
   status="Backup creato"
  }catch(e:Exception){status="Errore backup: ${e.message}"}
 }
 val restore=rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()){uri->
  if(uri!=null) try{
   val txt=context.contentResolver.openInputStream(uri)?.bufferedReader()?.use{it.readText()} ?: error("File vuoto")
   AppGraph.repository.restore(BackupCodec.decode(txt));status="Backup ripristinato"
  }catch(e:Exception){status="Errore ripristino: ${e.message}"}
 }
 Column(Modifier.padding(20.dp)){
  Text("Backup e ripristino",style=MaterialTheme.typography.headlineSmall);Spacer(Modifier.height(12.dp))
  Text("Esporta o ripristina i dati in formato JSON usando l'archivio documenti Android.")
  Button(onClick={create.launch("LivasODV_backup.json")}){Text("Crea backup")}
  OutlinedButton(onClick={restore.launch(arrayOf("application/json","text/plain"))}){Text("Ripristina backup")}
  status?.let{Text(it)}
 }
}
@Composable fun ExpiryScreen(){Text("Centro scadenze: mezzi, DPI, corsi e abilitazioni",Modifier.padding(24.dp))}
@Composable fun NotificationCenterScreen(){Text("Centro notifiche: comunicazioni, turni, richieste e scadenze",Modifier.padding(24.dp))}
@Composable fun EmergencyScreen(){Text("EMERGENZE\nFAQ di primo soccorso e numeri utili. Per emergenze reali utilizzare i numeri ufficiali.",Modifier.padding(24.dp))}
@Composable fun MonitorPSScreen(){Text("Monitor PS 118\nAdapter predisposto per le stesse fonti pubbliche/regionali della versione iPhone.",Modifier.padding(24.dp))}
@Composable fun CivilProtectionScreen(){Text("Protezione Civile\nAllerte meteo, incendi, numeri utili e comportamenti in emergenza. Adapter dati predisposto.",Modifier.padding(24.dp))}
@Composable fun RescueRunScreen(){RescueRunGame()}
