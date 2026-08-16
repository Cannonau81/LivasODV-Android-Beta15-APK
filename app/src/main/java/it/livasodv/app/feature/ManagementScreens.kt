package it.livasodv.app.feature
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.livasodv.app.data.*

@Composable fun SectionList(title:String,icon:@Composable()->Unit,rows:List<Pair<String,String>>,onAdd:(()->Unit)?=null){
 Scaffold(topBar={TopAppBar(title={Text(title)},actions={if(onAdd!=null)IconButton(onAdd){Icon(Icons.Default.Add,"Aggiungi")}})}){p->
  if(rows.isEmpty()) Box(Modifier.fillMaxSize().padding(p)){Text("Nessun elemento registrato",Modifier.padding(24.dp))}
  else LazyColumn(Modifier.fillMaxSize().padding(p),contentPadding=PaddingValues(12.dp)){items(rows){r->ListItem(headlineContent={Text(r.first)},supportingContent={Text(r.second)},leadingContent=icon);HorizontalDivider()}}
 }
}
@Composable fun MembersScreen(){
 val r=AppGraph.repository;val x by r.members.collectAsState();var add by remember{mutableStateOf(false)}
 SectionList("Soci",{Icon(Icons.Default.Group,null)},x.map{it.firstName+" "+it.lastName to it.role}){add=true}
 if(add){var nome by remember{mutableStateOf("")};var cognome by remember{mutableStateOf("")};AlertDialog(onDismissRequest={add=false},title={Text("Nuovo socio")},text={Column{OutlinedTextField(nome,{nome=it},label={Text("Nome")});OutlinedTextField(cognome,{cognome=it},label={Text("Cognome")})}},confirmButton={Button(onClick={if(nome.isNotBlank()&&cognome.isNotBlank())r.upsertMember(Member(java.util.UUID.randomUUID().toString(),nome,cognome));add=false}){Text("Salva")}},dismissButton={TextButton({add=false}){Text("Annulla")}})}
}
@Composable fun VehiclesScreen(){
 val r=AppGraph.repository;val x by r.vehicles.collectAsState();var add by remember{mutableStateOf(false)}
 SectionList("Mezzi",{Icon(Icons.Default.DirectionsCar,null)},x.map{it.name to (it.licensePlate.ifBlank{it.makeModel})}){add=true}
 if(add){var nome by remember{mutableStateOf("")};var targa by remember{mutableStateOf("")};AlertDialog(onDismissRequest={add=false},title={Text("Nuovo mezzo")},text={Column{OutlinedTextField(nome,{nome=it},label={Text("Nome mezzo")});OutlinedTextField(targa,{targa=it},label={Text("Targa")})}},confirmButton={Button(onClick={if(nome.isNotBlank())r.upsertVehicle(Vehicle(java.util.UUID.randomUUID().toString(),nome,licensePlate=targa));add=false}){Text("Salva")}},dismissButton={TextButton({add=false}){Text("Annulla")}})}
}
@Composable fun WarehouseScreen(){val r=AppGraph.repository;val x by r.warehouse.collectAsState();SectionList("Magazzino",{Icon(Icons.Default.Inventory2,null)},x.map{it.name to "Quantità: ${it.quantity}"}){}}
@Composable fun PresidiScreen(){val r=AppGraph.repository;val x by r.presidi.collectAsState();SectionList("Presidi",{Icon(Icons.Default.MedicalServices,null)},x.map{it.name to "Quantità: ${it.quantity}"}){}}
@Composable fun ShiftsScreen(){val r=AppGraph.repository;val x by r.shifts.collectAsState();SectionList("Turni",{Icon(Icons.Default.CalendarMonth,null)},x.map{it.title to "${it.date} ${it.start}"}){}}
@Composable fun ServicesScreen(){val r=AppGraph.repository;val x by r.services.collectAsState();SectionList("Servizi",{Icon(Icons.Default.MedicalServices,null)},x.map{it.title to "${it.fromPlace} → ${it.toPlace}"}){}}
@Composable fun CommunicationsScreen(){val r=AppGraph.repository;val x by r.communications.collectAsState();SectionList("Comunicazioni",{Icon(Icons.Default.Campaign,null)},x.map{it.title to it.date}){}}
@Composable fun CitizenRequestsScreen(){val r=AppGraph.repository;val x by r.citizenRequests.collectAsState();SectionList("Richieste cittadini",{Icon(Icons.Default.Person,null)},x.map{it.requester to "${it.kind} · ${it.status}"}){}}
@Composable fun CivilServiceScreen(){val r=AppGraph.repository;val x by r.civilVolunteers.collectAsState();SectionList("Servizio Civile",{Icon(Icons.Default.School,null)},x.map{it.firstName+" "+it.lastName to if(it.active)"Attivo" else "Non attivo"}){}}
@Composable fun AuditScreen(){val r=AppGraph.repository;val x by r.audit.collectAsState();SectionList("Registro attività",{Icon(Icons.Default.History,null)},x.map{it.action to "${it.area} · ${it.detail}"})}
@Composable fun MissionsScreen(){val r=AppGraph.repository;val x by r.missions.collectAsState();SectionList("Operativo",{Icon(Icons.Default.Emergency,null)},x.map{it.title to "${it.status} · ${it.location}"}){}}
