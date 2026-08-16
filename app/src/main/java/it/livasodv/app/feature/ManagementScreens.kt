package it.livasodv.app.feature

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.livasodv.app.data.AppGraph
import it.livasodv.app.data.Member
import it.livasodv.app.data.Vehicle
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionList(
    title: String,
    rows: List<Pair<String, String>>,
    icon: @Composable () -> Unit,
    onAdd: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    if (onAdd != null) {
                        IconButton(onClick = onAdd) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Aggiungi"
                            )
                        }
                    }
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

@Composable
fun MembersScreen() {
    val repo = AppGraph.repository
    val members by repo.members.collectAsState()

    var showAdd by remember { mutableStateOf(false) }

    SectionList(
        title = "Soci",
        rows = members.map {
            "${it.firstName} ${it.lastName}" to it.role
        },
        icon = {
            Icon(Icons.Default.Group, contentDescription = null)
        },
        onAdd = {
            showAdd = true
        }
    )

    if (showAdd) {
        var nome by remember { mutableStateOf("") }
        var cognome by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("Nuovo socio")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") }
                    )

                    OutlinedTextField(
                        value = cognome,
                        onValueChange = { cognome = it },
                        label = { Text("Cognome") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nome.isNotBlank() && cognome.isNotBlank()) {
                            repo.upsertMember(
                                Member(
                                    id = UUID.randomUUID().toString(),
                                    firstName = nome,
                                    lastName = cognome
                                )
                            )
                        }

                        showAdd = false
                    }
                ) {
                    Text("Salva")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("Annulla")
                }
            }
        )
    }
}

@Composable
fun VehiclesScreen() {
    val repo = AppGraph.repository
    val vehicles by repo.vehicles.collectAsState()

    var showAdd by remember { mutableStateOf(false) }

    SectionList(
        title = "Mezzi",
        rows = vehicles.map {
            it.name to it.licensePlate.ifBlank { it.makeModel }
        },
        icon = {
            Icon(Icons.Default.DirectionsCar, contentDescription = null)
        },
        onAdd = {
            showAdd = true
        }
    )

    if (showAdd) {
        var nome by remember { mutableStateOf("") }
        var targa by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                showAdd = false
            },
            title = {
                Text("Nuovo mezzo")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome mezzo") }
                    )

                    OutlinedTextField(
                        value = targa,
                        onValueChange = { targa = it },
                        label = { Text("Targa") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nome.isNotBlank()) {
                            repo.upsertVehicle(
                                Vehicle(
                                    id = UUID.randomUUID().toString(),
                                    name = nome,
                                    licensePlate = targa
                                )
                            )
                        }

                        showAdd = false
                    }
                ) {
                    Text("Salva")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAdd = false
                    }
                ) {
                    Text("Annulla")
                }
            }
        )
    }
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
            Icon(Icons.Default.Inventory2, contentDescription = null)
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
            Icon(Icons.Default.MedicalServices, contentDescription = null)
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
            Icon(Icons.Default.CalendarMonth, contentDescription = null)
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
            Icon(Icons.Default.MedicalServices, contentDescription = null)
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
            Icon(Icons.Default.Campaign, contentDescription = null)
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
            Icon(Icons.Default.Person, contentDescription = null)
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
            Icon(Icons.Default.School, contentDescription = null)
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
            Icon(Icons.Default.History, contentDescription = null)
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
            Icon(Icons.Default.Emergency, contentDescription = null)
        }
    )
}
