package it.livasodv.app.feature

import androidx.compose.foundation.clickable
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
import java.util.UUID

@Composable
private fun EmptyState(text: String) {
    Text(text, modifier = Modifier.padding(24.dp), color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
private fun ManagementHeader(title: String, subtitle: String? = null) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall)
        subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

private fun formatItalianDate(iso: String): String {
    val p = iso.split("-")
    return if (p.size == 3) "${p[2]}/${p[1]}/${p[0]}" else iso
}

private fun shortTime(value: String): String = if (value.length >= 5) value.substring(0, 5) else value

@Composable
fun MembersScreen() {
    val repo = AppGraph.repository
    val members by repo.members.collectAsState()
    var editing by remember { mutableStateOf<Member?>(null) }
    var adding by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.PersonAdd, null) }, text = { Text("Aggiungi socio") })
        }
    ) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Soci", "Aggiungi, modifica, elimina e gestisci ruoli") }
            if (members.isEmpty()) item { EmptyState("Nessun socio registrato") }
            items(members, key = { it.id }) { member ->
                ListItem(
                    modifier = Modifier.clickable { editing = member },
                    headlineContent = { Text("${member.firstName} ${member.lastName}") },
                    supportingContent = { Text(listOf(member.role, member.email, member.phone).filter { it.isNotBlank() }.joinToString(" · ")) },
                    leadingContent = { Icon(Icons.Default.Person, null) },
                    trailingContent = { IconButton(onClick = { editing = member }) { Icon(Icons.Default.Edit, "Modifica") } }
                )
                HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) MemberEditor(null, onDismiss = { adding = false }) { repo.upsertMember(it); adding = false }
    editing?.let { current ->
        MemberEditor(current, onDismiss = { editing = null }, onSave = { repo.upsertMember(it); editing = null }, onDelete = { repo.deleteMember(current.id); editing = null })
    }
}

@Composable
private fun MemberEditor(existing: Member?, onDismiss: () -> Unit, onSave: (Member) -> Unit, onDelete: (() -> Unit)? = null) {
    var firstName by remember(existing) { mutableStateOf(existing?.firstName ?: "") }
    var lastName by remember(existing) { mutableStateOf(existing?.lastName ?: "") }
    var email by remember(existing) { mutableStateOf(existing?.email ?: "") }
    var phone by remember(existing) { mutableStateOf(existing?.phone ?: "") }
    var role by remember(existing) { mutableStateOf(existing?.role ?: "Socio") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    var enabled118 by remember(existing) { mutableStateOf(existing?.enabled118 ?: false) }
    var pc by remember(existing) { mutableStateOf(existing?.civilProtection ?: false) }
    var aib by remember(existing) { mutableStateOf(existing?.aib ?: false) }
    var driver by remember(existing) { mutableStateOf(existing?.driver ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Nuovo socio" else "Modifica socio") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item { OutlinedTextField(firstName, { firstName = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(lastName, { lastName = it }, label = { Text("Cognome") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(phone, { phone = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth()) }
                item { OutlinedTextField(role, { role = it }, label = { Text("Ruolo") }, modifier = Modifier.fillMaxWidth()) }
                item { SwitchRow("Abilitato 118", enabled118) { enabled118 = it } }
                item { SwitchRow("Protezione Civile", pc) { pc = it } }
                item { SwitchRow("AIB", aib) { aib = it } }
                item { SwitchRow("Autista", driver) { driver = it } }
                item { OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
                if (onDelete != null) item { TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina socio", color = MaterialTheme.colorScheme.error) } }
            }
        },
        confirmButton = {
            Button(
                enabled = firstName.isNotBlank() && lastName.isNotBlank(),
                onClick = {
                    onSave(Member(existing?.id ?: UUID.randomUUID().toString(), firstName.trim(), lastName.trim(), phone.trim(), email.trim(), role.trim().ifBlank { "Socio" }, enabled118 = enabled118, civilProtection = pc, aib = aib, driver = driver, notes = notes.trim()))
                }
            ) { Text("Salva") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } }
    )
}

@Composable
private fun SwitchRow(label: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label)
        Switch(value, onCheckedChange = onChange)
    }
}

@Composable
fun VehiclesScreen() {
    val repo = AppGraph.repository
    val vehicles by repo.vehicles.collectAsState()
    var editing by remember { mutableStateOf<Vehicle?>(null) }
    var adding by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Aggiungi mezzo") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Mezzi", "Anagrafica, targa, chilometri e scadenze") }
            if (vehicles.isEmpty()) item { EmptyState("Nessun mezzo registrato") }
            items(vehicles, key = { it.id }) { v ->
                ListItem(
                    modifier = Modifier.clickable { editing = v },
                    headlineContent = { Text(v.name) },
                    supportingContent = { Text(listOf(v.makeModel, v.licensePlate, "${v.mileage} km").filter { it.isNotBlank() }.joinToString(" · ")) },
                    leadingContent = { Icon(Icons.Default.DirectionsCar, null) },
                    trailingContent = { IconButton(onClick = { editing = v }) { Icon(Icons.Default.Edit, "Modifica") } }
                ); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) VehicleEditor(null, { adding = false }) { repo.upsertVehicle(it); adding = false }
    editing?.let { v -> VehicleEditor(v, { editing = null }, { repo.upsertVehicle(it); editing = null }, { repo.deleteVehicle(v.id); editing = null }) }
}

@Composable
private fun VehicleEditor(existing: Vehicle?, onDismiss: () -> Unit, onSave: (Vehicle) -> Unit, onDelete: (() -> Unit)? = null) {
    var name by remember(existing) { mutableStateOf(existing?.name ?: "") }
    var model by remember(existing) { mutableStateOf(existing?.makeModel ?: "") }
    var plate by remember(existing) { mutableStateOf(existing?.licensePlate ?: "") }
    var km by remember(existing) { mutableStateOf(existing?.mileage?.toString() ?: "0") }
    var insurance by remember(existing) { mutableStateOf(existing?.insuranceExpiry ?: "") }
    var inspection by remember(existing) { mutableStateOf(existing?.inspectionExpiry ?: "") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing == null) "Nuovo mezzo" else "Modifica mezzo") },
        text = { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { OutlinedTextField(name, { name = it }, label = { Text("Nome mezzo") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(model, { model = it }, label = { Text("Marca / modello") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(plate, { plate = it.uppercase() }, label = { Text("Targa") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(km, { km = it.filter(Char::isDigit) }, label = { Text("Km attuali") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(insurance, { insurance = it }, label = { Text("Scadenza assicurazione AAAA-MM-GG") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(inspection, { inspection = it }, label = { Text("Scadenza revisione AAAA-MM-GG") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(notes, { notes = it }, label = { Text("Note / lavorazioni") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
            if (onDelete != null) item { TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina mezzo", color = MaterialTheme.colorScheme.error) } }
        } },
        confirmButton = { Button(enabled = name.isNotBlank(), onClick = { onSave(Vehicle(existing?.id ?: UUID.randomUUID().toString(), name.trim(), model.trim(), plate.trim(), insurance.takeIf { it.isNotBlank() }, inspection.takeIf { it.isNotBlank() }, km.toIntOrNull() ?: 0, notes.trim())) }) { Text("Salva") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } }
    )
}

@Composable
fun WarehouseScreen() {
    val repo = AppGraph.repository
    val entries by repo.warehouse.collectAsState()
    var editing by remember { mutableStateOf<WarehouseItem?>(null) }
    var adding by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Aggiungi articolo") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Magazzino", "Materiale, DPI, quantità e soglie minime") }
            if (entries.isEmpty()) item { EmptyState("Magazzino vuoto") }
            items(entries, key = { it.id }) { item ->
                ListItem(
                    modifier = Modifier.clickable { editing = item },
                    headlineContent = { Text(item.name) },
                    supportingContent = { Text("${item.category} · Quantità ${item.quantity} · Minimo ${item.minimum}") },
                    leadingContent = { Icon(Icons.Default.Inventory2, null) },
                    trailingContent = { IconButton(onClick = { editing = item }) { Icon(Icons.Default.Edit, "Modifica") } }
                ); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) WarehouseEditor(null, { adding = false }) { repo.upsertWarehouse(it); adding = false }
    editing?.let { item -> WarehouseEditor(item, { editing = null }, { repo.upsertWarehouse(it); editing = null }, { repo.deleteWarehouse(item.id); editing = null }) }
}

@Composable
private fun WarehouseEditor(existing: WarehouseItem?, onDismiss: () -> Unit, onSave: (WarehouseItem) -> Unit, onDelete: (() -> Unit)? = null) {
    var name by remember(existing) { mutableStateOf(existing?.name ?: "") }
    var category by remember(existing) { mutableStateOf(existing?.category ?: "DPI") }
    var quantity by remember(existing) { mutableStateOf(existing?.quantity?.toString() ?: "0") }
    var minimum by remember(existing) { mutableStateOf(existing?.minimum?.toString() ?: "0") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (existing == null) "Nuovo articolo" else "Modifica articolo") }, text = {
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item { OutlinedTextField(name, { name = it }, label = { Text("Articolo") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(category, { category = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(quantity, { quantity = it.filter(Char::isDigit) }, label = { Text("Quantità") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(minimum, { minimum = it.filter(Char::isDigit) }, label = { Text("Soglia minima") }, modifier = Modifier.fillMaxWidth()) }
            item { OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth()) }
            if (onDelete != null) item { TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina articolo", color = MaterialTheme.colorScheme.error) } }
        }
    }, confirmButton = { Button(enabled = name.isNotBlank(), onClick = { onSave(WarehouseItem(existing?.id ?: UUID.randomUUID().toString(), name.trim(), category.trim(), quantity.toIntOrNull() ?: 0, minimum.toIntOrNull() ?: 0, notes = notes.trim())) }) { Text("Salva") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } })
}

@Composable
fun ShiftsScreen() {
    val repo = AppGraph.repository
    val shifts by repo.shifts.collectAsState()
    var editing by remember { mutableStateOf<Shift?>(null) }
    var adding by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Nuovo turno") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Turni", "Crea, modifica ed elimina i turni dell'associazione") }
            if (shifts.isEmpty()) item { EmptyState("Nessun turno registrato") }
            items(shifts, key = { it.id }) { shift ->
                val start = shortTime(shift.start); val end = shortTime(shift.end)
                ListItem(
                    modifier = Modifier.clickable { editing = shift },
                    headlineContent = { Text(shift.title.ifBlank { "Turno" }) },
                    supportingContent = { Text("${formatItalianDate(shift.date)} · $start${if (end.isNotBlank()) "–$end" else ""}${if (shift.notes.isNotBlank()) "\n${shift.notes}" else ""}") },
                    leadingContent = { Icon(Icons.Default.CalendarMonth, null) },
                    trailingContent = { IconButton(onClick = { editing = shift }) { Icon(Icons.Default.Edit, "Modifica") } }
                ); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) ShiftEditor(null, { adding = false }) { repo.upsertShift(it); adding = false }
    editing?.let { s -> ShiftEditor(s, { editing = null }, { repo.upsertShift(it); editing = null }, { repo.deleteShift(s.id); editing = null }) }
}

@Composable
private fun ShiftEditor(existing: Shift?, onDismiss: () -> Unit, onSave: (Shift) -> Unit, onDelete: (() -> Unit)? = null) {
    var area by remember(existing) { mutableStateOf(existing?.title ?: "118") }
    var date by remember(existing) { mutableStateOf(existing?.date ?: "") }
    var start by remember(existing) { mutableStateOf(existing?.start ?: "") }
    var end by remember(existing) { mutableStateOf(existing?.end ?: "") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (existing == null) "Nuovo turno" else "Modifica turno") }, text = { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(area, { area = it }, label = { Text("Area (118 / PC / AIB / Sociale)") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(date, { date = it }, label = { Text("Data AAAA-MM-GG") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(start, { start = it }, label = { Text("Inizio HH:MM") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(end, { end = it }, label = { Text("Fine HH:MM") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
        if (onDelete != null) item { TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina turno", color = MaterialTheme.colorScheme.error) } }
    } }, confirmButton = { Button(enabled = area.isNotBlank() && date.isNotBlank(), onClick = { onSave(Shift(existing?.id ?: UUID.randomUUID().toString(), area.trim(), date.trim(), start.trim(), end.trim(), notes = notes.trim())) }) { Text("Salva") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } })
}

@Composable
fun ServicesScreen() {
    val repo = AppGraph.repository
    val services by repo.services.collectAsState()
    var editing by remember { mutableStateOf<Service?>(null) }
    var adding by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Nuovo servizio") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Servizi", "Accompagnamenti, visite, ricoveri, dimissioni ed eventi") }
            if (services.isEmpty()) item { EmptyState("Nessun servizio registrato") }
            items(services, key = { it.id }) { s ->
                ListItem(modifier = Modifier.clickable { editing = s }, headlineContent = { Text(s.title) }, supportingContent = { Text("${formatItalianDate(s.date.take(10))} · ${s.fromPlace} → ${s.toPlace}${if (s.notes.isNotBlank()) "\n${s.notes}" else ""}") }, leadingContent = { Icon(Icons.Default.MedicalServices, null) }, trailingContent = { IconButton(onClick = { editing = s }) { Icon(Icons.Default.Edit, "Modifica") } }); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) ServiceEditor(null, { adding = false }) { repo.upsertService(it); adding = false }
    editing?.let { s -> ServiceEditor(s, { editing = null }, { repo.upsertService(it); editing = null }, { repo.deleteService(s.id); editing = null }) }
}

@Composable
private fun ServiceEditor(existing: Service?, onDismiss: () -> Unit, onSave: (Service) -> Unit, onDelete: (() -> Unit)? = null) {
    var title by remember(existing) { mutableStateOf(existing?.title ?: "") }
    var date by remember(existing) { mutableStateOf(existing?.date ?: "") }
    var from by remember(existing) { mutableStateOf(existing?.fromPlace ?: "") }
    var to by remember(existing) { mutableStateOf(existing?.toPlace ?: "") }
    var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (existing == null) "Nuovo servizio" else "Modifica servizio") }, text = { LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item { OutlinedTextField(title, { title = it }, label = { Text("Tipo / titolo") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(date, { date = it }, label = { Text("Data / ora") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(from, { from = it }, label = { Text("Da") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(to, { to = it }, label = { Text("A") }, modifier = Modifier.fillMaxWidth()) }
        item { OutlinedTextField(notes, { notes = it }, label = { Text("Note, mobilità, scale, presidi") }, modifier = Modifier.fillMaxWidth(), minLines = 2) }
        if (onDelete != null) item { TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina servizio", color = MaterialTheme.colorScheme.error) } }
    } }, confirmButton = { Button(enabled = title.isNotBlank(), onClick = { onSave(Service(existing?.id ?: UUID.randomUUID().toString(), title.trim(), date.trim(), from.trim(), to.trim(), notes = notes.trim())) }) { Text("Salva") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } })
}

@Composable
fun CommunicationsScreen() {
    val repo = AppGraph.repository
    val communications by repo.communications.collectAsState()
    var adding by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Communication?>(null) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Nuova comunicazione") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Comunicazioni", "Avvisi del Direttivo e news") }
            if (communications.isEmpty()) item { EmptyState("Nessuna comunicazione") }
            items(communications, key = { it.id }) { c ->
                ListItem(modifier = Modifier.clickable { editing = c }, headlineContent = { Text(c.title) }, supportingContent = { Text(c.body) }, leadingContent = { Icon(if (c.urgent) Icons.Default.Warning else Icons.Default.Campaign, null) }, trailingContent = { IconButton(onClick = { editing = c }) { Icon(Icons.Default.Edit, "Modifica") } }); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) CommunicationEditor(null, { adding = false }) { repo.upsertCommunication(it); adding = false }
    editing?.let { c -> CommunicationEditor(c, { editing = null }, { repo.upsertCommunication(it); editing = null }, { repo.deleteCommunication(c.id); editing = null }) }
}

@Composable
private fun CommunicationEditor(existing: Communication?, onDismiss: () -> Unit, onSave: (Communication) -> Unit, onDelete: (() -> Unit)? = null) {
    var title by remember(existing) { mutableStateOf(existing?.title ?: "") }
    var body by remember(existing) { mutableStateOf(existing?.body ?: "") }
    var urgent by remember(existing) { mutableStateOf(existing?.urgent ?: false) }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (existing == null) "Nuova comunicazione" else "Modifica comunicazione") }, text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(title, { title = it }, label = { Text("Titolo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(body, { body = it }, label = { Text("Testo") }, modifier = Modifier.fillMaxWidth(), minLines = 4)
        SwitchRow("Urgente", urgent) { urgent = it }
        if (onDelete != null) TextButton(onClick = onDelete) { Icon(Icons.Default.Delete, null); Spacer(Modifier.width(6.dp)); Text("Elimina", color = MaterialTheme.colorScheme.error) }
    } }, confirmButton = { Button(enabled = title.isNotBlank(), onClick = { onSave(Communication(existing?.id ?: UUID.randomUUID().toString(), title.trim(), body.trim(), existing?.date ?: java.time.Instant.now().toString(), urgent)) }) { Text("Salva") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } })
}

@Composable
fun PresidiScreen() {
    val repo = AppGraph.repository
    val entries by repo.presidi.collectAsState()
    var adding by remember { mutableStateOf(false) }
    var editing by remember { mutableStateOf<Presidio?>(null) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Aggiungi presidio") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Presidi", "Carrozzine, letti, materassini, stampelle e ausili") }
            if (entries.isEmpty()) item { EmptyState("Nessun presidio registrato") }
            items(entries, key = { it.id }) { p ->
                ListItem(modifier = Modifier.clickable { editing = p }, headlineContent = { Text(p.name) }, supportingContent = { Text("${p.category} · Quantità ${p.quantity} · ${if (p.available) "Disponibile" else "Non disponibile"}") }, leadingContent = { Icon(Icons.Default.HealthAndSafety, null) }, trailingContent = { IconButton(onClick = { editing = p }) { Icon(Icons.Default.Edit, "Modifica") } }); HorizontalDivider()
            }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) PresidioEditor(null, { adding = false }) { repo.upsertPresidio(it); adding = false }
    editing?.let { p -> PresidioEditor(p, { editing = null }, { repo.upsertPresidio(it); editing = null }, { repo.deletePresidio(p.id); editing = null }) }
}

@Composable
private fun PresidioEditor(existing: Presidio?, onDismiss: () -> Unit, onSave: (Presidio) -> Unit, onDelete: (() -> Unit)? = null) {
    var name by remember(existing) { mutableStateOf(existing?.name ?: "") }; var category by remember(existing) { mutableStateOf(existing?.category ?: "Ausilio") }; var qty by remember(existing) { mutableStateOf(existing?.quantity?.toString() ?: "0") }; var available by remember(existing) { mutableStateOf(existing?.available ?: true) }; var notes by remember(existing) { mutableStateOf(existing?.notes ?: "") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (existing == null) "Nuovo presidio" else "Modifica presidio") }, text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(name, { name = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()); OutlinedTextField(category, { category = it }, label = { Text("Categoria") }, modifier = Modifier.fillMaxWidth()); OutlinedTextField(qty, { qty = it.filter(Char::isDigit) }, label = { Text("Quantità") }, modifier = Modifier.fillMaxWidth()); SwitchRow("Disponibile", available) { available = it }; OutlinedTextField(notes, { notes = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth()); if (onDelete != null) TextButton(onClick = onDelete) { Text("Elimina presidio", color = MaterialTheme.colorScheme.error) }
    } }, confirmButton = { Button(enabled = name.isNotBlank(), onClick = { onSave(Presidio(existing?.id ?: UUID.randomUUID().toString(), name.trim(), category.trim(), qty.toIntOrNull() ?: 0, available, notes.trim())) }) { Text("Salva") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("Annulla") } })
}

@Composable
fun CitizenRequestsScreen() {
    val repo = AppGraph.repository
    val requests by repo.citizenRequests.collectAsState()
    LazyColumn(Modifier.fillMaxSize()) {
        item { ManagementHeader("Richieste cittadini", "Prenotazioni, accompagnamenti e richieste presidi") }
        if (requests.isEmpty()) item { EmptyState("Nessuna richiesta") }
        items(requests, key = { it.id }) { r ->
            ListItem(headlineContent = { Text(r.requester) }, supportingContent = { Text("${r.kind} · ${r.status}${if (r.phone.isNotBlank()) " · ${r.phone}" else ""}\n${r.details}") }, leadingContent = { Icon(Icons.Default.Person, null) }); HorizontalDivider()
        }
    }
}

@Composable
fun CivilServiceScreen() {
    val repo = AppGraph.repository
    val volunteers by repo.civilVolunteers.collectAsState()
    var adding by remember { mutableStateOf(false) }
    Scaffold(floatingActionButton = { ExtendedFloatingActionButton(onClick = { adding = true }, icon = { Icon(Icons.Default.PersonAdd, null) }, text = { Text("Aggiungi operatore") }) }) { padding ->
        LazyColumn(Modifier.fillMaxSize().padding(padding)) {
            item { ManagementHeader("Servizio Civile · OLP", "Operatori, turni, corsi, ferie, permessi e malattia") }
            if (volunteers.isEmpty()) item { EmptyState("Nessun operatore registrato") }
            items(volunteers, key = { it.id }) { v -> ListItem(headlineContent = { Text("${v.firstName} ${v.lastName}") }, supportingContent = { Text(if (v.active) "Attivo" else "Non attivo") }, leadingContent = { Icon(Icons.Default.School, null) }); HorizontalDivider() }
            item { Spacer(Modifier.height(90.dp)) }
        }
    }
    if (adding) {
        var first by remember { mutableStateOf("") }; var last by remember { mutableStateOf("") }
        AlertDialog(onDismissRequest = { adding = false }, title = { Text("Nuovo operatore Servizio Civile") }, text = { Column { OutlinedTextField(first, { first = it }, label = { Text("Nome") }); OutlinedTextField(last, { last = it }, label = { Text("Cognome") }) } }, confirmButton = { Button(enabled = first.isNotBlank() && last.isNotBlank(), onClick = { repo.upsertCivilVolunteer(CivilVolunteer(UUID.randomUUID().toString(), first.trim(), last.trim())); adding = false }) { Text("Salva") } }, dismissButton = { TextButton(onClick = { adding = false }) { Text("Annulla") } })
    }
}

@Composable
fun AuditScreen() { val r = AppGraph.repository; val x by r.audit.collectAsState(); SimpleReadOnly("Registro attività", x.map { it.action to "${it.area} · ${it.detail}" }, Icons.Default.History) }
@Composable
fun MissionsScreen() { val r = AppGraph.repository; val x by r.missions.collectAsState(); SimpleReadOnly("Operativo", x.map { it.title to "${it.status} · ${it.location}" }, Icons.Default.Emergency) }

@Composable
private fun SimpleReadOnly(title: String, rows: List<Pair<String, String>>, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    LazyColumn(Modifier.fillMaxSize()) {
        item { ManagementHeader(title) }
        if (rows.isEmpty()) item { EmptyState("Nessun elemento registrato") }
        items(rows) { row -> ListItem(headlineContent = { Text(row.first) }, supportingContent = { Text(row.second) }, leadingContent = { Icon(icon, null) }); HorizontalDivider() }
    }
}
