package it.livasodv.app.feature

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.livasodv.app.data.AppGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionList(
    title: String,
    rows: List<Pair<String, String>>,
    icon: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(title) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(12.dp)
        ) {
            if (rows.isEmpty()) {
                item {
                    Text(
                        text = "Nessun elemento registrato",
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                items(rows) { row ->
                    ListItem(
                        headlineContent = { Text(row.first) },
                        supportingContent = { Text(row.second) },
                        leadingContent = icon
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

private fun formatItalianDate(iso: String): String {
    val p = iso.split("-")
    return if (p.size == 3) "${p[2]}/${p[1]}/${p[0]}" else iso
}

private fun shortTime(value: String): String =
    if (value.length >= 5) value.substring(0, 5) else value

@Composable
fun MembersScreen() {
    val repo = AppGraph.repository
    val members by repo.members.collectAsState()
    SectionList(
        "Soci",
        members.map { "${it.firstName} ${it.lastName}" to it.role },
        { Icon(Icons.Default.Group, null) }
    )
}

@Composable
fun VehiclesScreen() {
    val repo = AppGraph.repository
    val vehicles by repo.vehicles.collectAsState()
    SectionList(
        "Mezzi",
        vehicles.map { it.name to it.licensePlate.ifBlank { it.makeModel } },
        { Icon(Icons.Default.DirectionsCar, null) }
    )
}

@Composable
fun WarehouseScreen() {
    val repo = AppGraph.repository
    val entries by repo.warehouse.collectAsState()
    SectionList(
        "Magazzino",
        entries.map { it.name to "Quantità: ${it.quantity}" },
        { Icon(Icons.Default.Inventory2, null) }
    )
}

@Composable
fun PresidiScreen() {
    val repo = AppGraph.repository
    val entries by repo.presidi.collectAsState()
    SectionList(
        "Presidi",
        entries.map { it.name to "Quantità: ${it.quantity}" },
        { Icon(Icons.Default.MedicalServices, null) }
    )
}

@Composable
fun ShiftsScreen() {
    val repo = AppGraph.repository
    val shifts by repo.shifts.collectAsState()

    SectionList(
        title = "Turni",
        rows = shifts.map { shift ->
            val date = formatItalianDate(shift.date)
            val start = shortTime(shift.start)
            val end = shortTime(shift.end)
            val time = when {
                start.isNotBlank() && end.isNotBlank() -> "$start–$end"
                start.isNotBlank() -> start
                else -> "Orario da definire"
            }
            val note = shift.notes.trim()
            val detail = buildString {
                append("$date · $time")
                if (note.isNotBlank()) append("\n$note")
            }
            (if (shift.title.isBlank()) "Turno" else shift.title) to detail
        },
        icon = { Icon(Icons.Default.CalendarMonth, null) }
    )
}

@Composable
fun ServicesScreen() {
    val repo = AppGraph.repository
    val services by repo.services.collectAsState()
    SectionList(
        "Servizi",
        services.map { it.title to "${it.fromPlace} → ${it.toPlace}" },
        { Icon(Icons.Default.MedicalServices, null) }
    )
}

@Composable
fun CommunicationsScreen() {
    val repo = AppGraph.repository
    val communications by repo.communications.collectAsState()
    SectionList(
        "Comunicazioni",
        communications.map { it.title to it.date },
        { Icon(Icons.Default.Campaign, null) }
    )
}

@Composable
fun CitizenRequestsScreen() {
    val repo = AppGraph.repository
    val requests by repo.citizenRequests.collectAsState()
    SectionList(
        "Richieste cittadini",
        requests.map { it.requester to "${it.kind} · ${it.status}" },
        { Icon(Icons.Default.Person, null) }
    )
}

@Composable
fun CivilServiceScreen() {
    val repo = AppGraph.repository
    val volunteers by repo.civilVolunteers.collectAsState()
    SectionList(
        "Servizio Civile",
        volunteers.map {
            "${it.firstName} ${it.lastName}" to
                if (it.active) "Attivo" else "Non attivo"
        },
        { Icon(Icons.Default.School, null) }
    )
}

@Composable
fun AuditScreen() {
    val repo = AppGraph.repository
    val events by repo.audit.collectAsState()
    SectionList(
        "Registro attività",
        events.map { it.action to "${it.area} · ${it.detail}" },
        { Icon(Icons.Default.History, null) }
    )
}

@Composable
fun MissionsScreen() {
    val repo = AppGraph.repository
    val missions by repo.missions.collectAsState()
    SectionList(
        "Operativo",
        missions.map { it.title to "${it.status} · ${it.location}" },
        { Icon(Icons.Default.Emergency, null) }
    )
}
