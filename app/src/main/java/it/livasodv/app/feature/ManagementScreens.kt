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
            TopAppBar(
                title = {
                    Text(title)
                }
            )
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
                        headlineContent = {
                            Text(row.first)
                        },
                        supportingContent = {
                            Text(row.second)
                        },
                        leadingContent = icon
                    )

                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun MembersScreen() {
    val repo = AppGraph.repository
    val members by repo.members.collectAsState()

    SectionList(
        title = "Soci",
        rows = members.map {
            "${it.firstName} ${it.lastName}" to it.role
        },
        icon = {
            Icon(
                Icons.Default.Group,
                contentDescription = null
            )
        }
    )
}

@Composable
fun VehiclesScreen() {
    val repo = AppGraph.repository
    val vehicles by repo.vehicles.collectAsState()

    SectionList(
        title = "Mezzi",
        rows = vehicles.map {
            it.name to it.licensePlate.ifBlank {
                it.makeModel
            }
        },
        icon = {
            Icon(
                Icons.Default.DirectionsCar,
                contentDescription = null
            )
        }
    )
}

@Composable
fun WarehouseScreen() {
    val repo = AppGraph.repository
    val items by repo.warehouse.collectAsState()

    SectionList(
        title = "Magazzino",
        rows = items.map {
            it.name to "Quantità: ${it.quantity}"
        },
        icon = {
            Icon(
                Icons.Default.Inventory2,
                contentDescription = null
            )
        }
    )
}

@Composable
fun PresidiScreen() {
    val repo = AppGraph.repository
    val items by repo.presidi.collectAsState()

    SectionList(
        title = "Presidi",
        rows = items.map {
            it.name to "Quantità: ${it.quantity}"
        },
        icon = {
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null
            )
        }
    )
}

@Composable
fun ShiftsScreen() {
    val repo = AppGraph.repository
    val shifts by repo.shifts.collectAsState()

    SectionList(
        title = "Turni",
        rows = shifts.map {
            it.title to "${it.date} ${it.start}"
        },
        icon = {
            Icon(
                Icons.Default.CalendarMonth,
                contentDescription = null
            )
        }
    )
}

@Composable
fun ServicesScreen() {
    val repo = AppGraph.repository
    val services by repo.services.collectAsState()

    SectionList(
        title = "Servizi",
        rows = services.map {
            it.title to "${it.fromPlace} → ${it.toPlace}"
        },
        icon = {
            Icon(
                Icons.Default.MedicalServices,
                contentDescription = null
            )
        }
    )
}

@Composable
fun CommunicationsScreen() {
    val repo = AppGraph.repository
    val communications by repo.communications.collectAsState()

    SectionList(
        title = "Comunicazioni",
        rows = communications.map {
            it.title to it.date
        },
        icon = {
            Icon(
                Icons.Default.Campaign,
                contentDescription = null
            )
        }
    )
}

@Composable
fun CitizenRequestsScreen() {
    val repo = AppGraph.repository
    val requests by repo.citizenRequests.collectAsState()

    SectionList(
        title = "Richieste cittadini",
        rows = requests.map {
            it.requester to "${it.kind} · ${it.status}"
        },
        icon = {
            Icon(
                Icons.Default.Person,
                contentDescription = null
            )
        }
    )
}

@Composable
fun CivilServiceScreen() {
    val repo = AppGraph.repository
    val volunteers by repo.civilVolunteers.collectAsState()

    SectionList(
        title = "Servizio Civile",
        rows = volunteers.map {
            "${it.firstName} ${it.lastName}" to
                if (it.active) "Attivo" else "Non attivo"
        },
        icon = {
            Icon(
                Icons.Default.School,
                contentDescription = null
            )
        }
    )
}

@Composable
fun AuditScreen() {
    val repo = AppGraph.repository
    val events by repo.audit.collectAsState()

    SectionList(
        title = "Registro attività",
        rows = events.map {
            it.action to "${it.area} · ${it.detail}"
        },
        icon = {
            Icon(
                Icons.Default.History,
                contentDescription = null
            )
        }
    )
}

@Composable
fun MissionsScreen() {
    val repo = AppGraph.repository
    val missions by repo.missions.collectAsState()

    SectionList(
        title = "Operativo",
        rows = missions.map {
            it.title to "${it.status} · ${it.location}"
        },
        icon = {
            Icon(
                Icons.Default.Emergency,
                contentDescription = null
            )
        }
    )
}
