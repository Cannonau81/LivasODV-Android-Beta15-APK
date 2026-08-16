package it.livasodv.app.feature
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.livasodv.app.data.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionList(title:String, rows:List<Pair<String,String>>, icon:@Composable()->Unit, onAdd:(()->Unit)?=null) {
    Scaffold(topBar={TopAppBar(title={Text(title)},actions={if(onAdd!=null)IconButton(onClick=onAdd){Icon(Icons.Default.Add,"Aggiungi")}})}) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding),contentPadding=PaddingValues(12.dp)) {
            if(rows.isEmpty()) item { Text("Nessun elemento registrato",Modifier.padding(24.dp)) }
            else items(rows) { row ->
                ListItem(headlineContent={Text(row.first)},supportingContent={Text(row.second)},leadingContent=icon)
                HorizontalDivider()
            }
        }
    }
}
@Composable fun MembersScreen(){val r=AppGraph.repository;val x by r.members.collectAsState();SectionList("Soci",x.map{"${it.firstName} ${it.lastName}" to it.role},{Icon(Icons.Default.Group,null)})}
@Composable fun VehiclesScreen(){val r=AppGraph.repository;val x by r.vehicles.collectAsState();SectionList("Mezzi",x.map{it.name to it.licensePlate.ifBlank{it.makeModel}},{Icon(Icons.Default.DirectionsCar,null)})}
@Composable fun WarehouseScreen(){val r=AppGraph.repository;val x by r.warehouse.collectAsState();SectionList("Magazzino",x.map{it.name to "Quantità: ${it.quantity}"},{Icon(Icons.Default.Inventory2,null)})}
@Composable fun PresidiScreen(){val r=AppGraph.repository;val x by r.presidi.collectAsState();SectionList("Presidi",x.map{it.name to "Quantità: ${it.quantity}"},{Icon(Icons.Default.MedicalServices,null)})}
@Composable fun ShiftsScreen(){val r=AppGraph.repository;val x by r.shifts.collectAsState();SectionList("Turni",x.map{it.title to "${it.date} ${it.start}"},{Icon(Icons.Default.CalendarMonth,null)})}
@Composable fun ServicesScreen(){val r=AppGraph.repository;val x by r.services.collectAsState();SectionList("Servizi",x.map{it.title to "${it.fromPlace} → ${it.toPlace}"},{Icon(Icons.Default.MedicalServices,null)})}
@Composable fun CommunicationsScreen(){val r=AppGraph.repository;val x by r.communications.collectAsState();SectionList("Comunicazioni",x.map{it.title to it.date},{Icon(Icons.Default.Campaign,null)})}
@Composable fun CitizenRequestsScreen(){val r=AppGraph.repository;val x by r.citizenRequests.collectAsState();SectionList("Richieste cittadini",x.map{it.requester to "${it.kind} · ${it.status}"},{Icon(Icons.Default.Person,null)})}
@Composable fun CivilServiceScreen(){val r=AppGraph.repository;val x by r.civilVolunteers.collectAsState();SectionList("Servizio Civile",x.map{"${it.firstName} ${it.lastName}" to if(it.active)"Attivo" else "Non attivo"},{Icon(Icons.Default.School,null)})}
@Composable fun AuditScreen(){val r=AppGraph.repository;val x by r.audit.collectAsState();SectionList("Registro attività",x.map{it.action to "${it.area} · ${it.detail}"},{Icon(Icons.Default.History,null)})}
@Composable fun MissionsScreen(){val r=AppGraph.repository;val x by r.missions.collectAsState();SectionList("Operativo",x.map{it.title to "${it.status} · ${it.location}"},{Icon(Icons.Default.Emergency,null)})}
