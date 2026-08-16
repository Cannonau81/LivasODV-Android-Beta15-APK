package it.livasodv.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.livasodv.app.data.SupabaseSync
import it.livasodv.app.feature.*
import it.livasodv.app.ui.theme.LivasTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { LivasTheme { LivasAndroidApp() } }
    }
}

enum class MainTab { DIRETTIVO, SOCI, SERVIZI, MEZZI, ALTRO }

data class HomeTool(val route: String, val title: String, val subtitle: String, val icon: ImageVector)

@Composable
fun LivasAndroidApp() {
    var tab by remember { mutableStateOf(MainTab.DIRETTIVO) }
    var route by remember { mutableStateOf<String?>(null) }
    var syncError by remember { mutableStateOf<String?>(null) }
    var syncing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun refresh() {
        scope.launch {
            syncing = true
            try {
                SupabaseSync().refreshCore()
                syncError = null
            } catch (e: Exception) {
                syncError = e.message
            } finally {
                syncing = false
            }
        }
    }

    LaunchedEffect(Unit) { refresh() }

    if (route != null) {
        BackHandler { route = null }
        RoutedScreen(
            route = route!!,
            offline = syncError != null,
            syncing = syncing,
            onBack = { route = null },
            onRefresh = { refresh() }
        )
        return
    }

    Scaffold(
        containerColor = Color(0xFF050505),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF111111)) {
                NavigationBarItem(
                    selected = tab == MainTab.DIRETTIVO,
                    onClick = { tab = MainTab.DIRETTIVO },
                    icon = { Icon(Icons.Default.Groups, null) },
                    label = { Text("Direttivo") }
                )
                NavigationBarItem(
                    selected = tab == MainTab.SOCI,
                    onClick = { tab = MainTab.SOCI },
                    icon = { Icon(Icons.Default.People, null) },
                    label = { Text("Soci") }
                )
                NavigationBarItem(
                    selected = tab == MainTab.SERVIZI,
                    onClick = { tab = MainTab.SERVIZI },
                    icon = { Icon(Icons.Default.MedicalServices, null) },
                    label = { Text("Servizi") }
                )
                NavigationBarItem(
                    selected = tab == MainTab.MEZZI,
                    onClick = { tab = MainTab.MEZZI },
                    icon = { Icon(Icons.Default.DirectionsCar, null) },
                    label = { Text("Mezzi") }
                )
                NavigationBarItem(
                    selected = tab == MainTab.ALTRO,
                    onClick = { tab = MainTab.ALTRO },
                    icon = { Icon(Icons.Default.MoreHoriz, null) },
                    label = { Text("Altro") }
                )
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when (tab) {
                MainTab.DIRETTIVO -> DirectorDashboard(
                    offline = syncError != null,
                    syncing = syncing,
                    onOpen = { route = it },
                    onRefresh = { refresh() }
                )
                MainTab.SOCI -> MembersScreen()
                MainTab.SERVIZI -> ServicesScreen()
                MainTab.MEZZI -> VehiclesScreen()
                MainTab.ALTRO -> MoreArea(onOpen = { route = it })
            }
        }
    }
}

@Composable
private fun RoutedScreen(
    route: String,
    offline: Boolean,
    syncing: Boolean,
    onBack: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(Modifier.fillMaxSize().background(Color(0xFF050505))) {
        Surface(color = Color(0xFF111111), shadowElevation = 4.dp) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Indietro") }
                Text("LÌ.V.A.S.", fontWeight = FontWeight.Black)
                Spacer(Modifier.weight(1f))
                if (offline) Text("OFFLINE", color = Color(0xFFFFA000), fontSize = 11.sp)
                IconButton(onClick = onRefresh, enabled = !syncing) {
                    Icon(Icons.Default.Refresh, "Aggiorna")
                }
            }
        }
        Box(Modifier.weight(1f)) {
            when (route) {
                "turni" -> ShiftsScreen()
                "servizi" -> ServicesScreen()
                "magazzino" -> WarehouseScreen()
                "mezzi" -> VehiclesScreen()
                "soci" -> MembersScreen()
                "comunicazioni" -> CommunicationsScreen()
                "presidi" -> PresidiScreen()
                "sc" -> CivilServiceScreen()
                "cittadini" -> CitizenRequestsScreen()
                "registro" -> AuditScreen()
                "operativo" -> MissionsScreen()
                "ricerca" -> GlobalSearchScreen()
                "backup" -> BackupScreen()
                "scadenze" -> ExpiryScreen()
                "notifiche" -> NotificationCenterScreen()
                "emergenze" -> EmergencyScreen()
                "ps" -> MonitorPSScreen()
                "pc" -> CivilProtectionScreen()
                "game" -> RescueRunScreen()
                else -> Text("Sezione non disponibile", Modifier.padding(24.dp))
            }
        }
    }
}

@Composable
fun DirectorDashboard(
    offline: Boolean,
    syncing: Boolean,
    onOpen: (String) -> Unit,
    onRefresh: () -> Unit
) {
    val tools = listOf(
        HomeTool("turni", "I MIEI TURNI", "Gestione e assegnazioni", Icons.Default.CalendarMonth),
        HomeTool("servizi", "SERVIZI SOCIALI", "Richieste e servizi", Icons.Default.MedicalServices),
        HomeTool("magazzino", "MAGAZZINO", "Materiale e DPI", Icons.Default.Inventory2),
        HomeTool("mezzi", "MEZZI", "Gestione e scadenze", Icons.Default.DirectionsCar),
        HomeTool("soci", "SOCI", "Elenco e ruoli", Icons.Default.Groups),
        HomeTool("comunicazioni", "COMUNICAZIONI", "Avvisi e news", Icons.Default.Campaign),
        HomeTool("presidi", "PRESIDI", "Ausili e dotazioni", Icons.Default.HealthAndSafety),
        HomeTool("sc", "SERVIZIO CIVILE", "OLP, turni e corsi", Icons.Default.School),
        HomeTool("cittadini", "RICHIESTE CITTADINI", "Prenotazioni e presidi", Icons.Default.Person),
        HomeTool("operativo", "OPERATIVO", "Missioni", Icons.Default.Emergency)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFF030303), Color(0xFF160707), Color(0xFF030303)))
        ),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column(
                Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(R.drawable.livas_logo),
                    contentDescription = "Logo Lì.v.a.s.",
                    modifier = Modifier.size(205.dp),
                    contentScale = ContentScale.Fit
                )
                Text("LÌ.V.A.S. O.D.V.", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color.White)
                Text("GONNOSFANADIGA", color = Color(0xFFFF3B30), fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                Text("Insieme per aiutare, sempre.", color = Color(0xFFAAAAAA), fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (offline) Text("OFFLINE · dati locali", color = Color(0xFFFFA000), fontSize = 11.sp)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onRefresh, enabled = !syncing) {
                        Icon(Icons.Default.Refresh, null)
                        Spacer(Modifier.width(4.dp))
                        Text(if (syncing) "Sincronizzo…" else "Aggiorna")
                    }
                }
            }
        }
        items(tools) { tool -> DashboardTile(tool = tool, onClick = { onOpen(tool.route) }) }
        item(span = { GridItemSpan(2) }) {
            Text(
                "ANPAS  •  PROTEZIONE CIVILE  •  REGIONE SARDEGNA  •  LÌ.V.A.S.\nInsieme per aiutare, sempre.",
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                color = Color(0xFF999999), fontSize = 10.sp, textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DashboardTile(tool: HomeTool, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(140.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xEE111111)),
        border = BorderStroke(1.dp, Color(0xFFFF3030)),
        shape = RoundedCornerShape(22.dp)
    ) {
        Column(
            Modifier.fillMaxSize().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier.size(54.dp).background(Color(0xFFB51616), CircleShape),
                contentAlignment = Alignment.Center
            ) { Icon(tool.icon, null, tint = Color.White, modifier = Modifier.size(29.dp)) }
            Spacer(Modifier.height(8.dp))
            Text(tool.title, color = Color.White, fontWeight = FontWeight.Black, fontSize = 13.sp, textAlign = TextAlign.Center)
            Text(tool.subtitle, color = Color(0xFF9A9A9A), fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MoreArea(onOpen: (String) -> Unit) {
    val tools = listOf(
        HomeTool("magazzino", "Magazzino", "Materiale, DPI e consegne", Icons.Default.Inventory2),
        HomeTool("sc", "Servizio Civile", "OLP, operatori, turni e corsi", Icons.Default.School),
        HomeTool("comunicazioni", "Comunicazioni", "Avvisi e news", Icons.Default.Campaign),
        HomeTool("cittadini", "Richieste cittadini", "Accompagnamenti e presidi", Icons.Default.Person),
        HomeTool("turni", "Turni", "Calendario associazione", Icons.Default.CalendarMonth),
        HomeTool("presidi", "Presidi", "Ausili e dotazioni", Icons.Default.HealthAndSafety),
        HomeTool("ricerca", "Ricerca", "Soci, mezzi, servizi e materiali", Icons.Default.Search),
        HomeTool("backup", "Backup", "Esporta e ripristina", Icons.Default.Backup),
        HomeTool("scadenze", "Scadenze", "Mezzi, DPI, corsi", Icons.Default.EventBusy),
        HomeTool("registro", "Registro", "Attività e controlli", Icons.Default.History),
        HomeTool("emergenze", "Emergenze", "FAQ e primo soccorso", Icons.Default.Phone),
        HomeTool("ps", "Monitor PS 118", "Pronto Soccorso Sardegna", Icons.Default.MonitorHeart),
        HomeTool("pc", "Protezione Civile", "Allerte e incendi", Icons.Default.Cloud),
        HomeTool("game", "Passatempo", "Rescue Run", Icons.Default.SportsEsports)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().background(Color(0xFF050505)),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text("Altro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Black, modifier = Modifier.padding(vertical = 8.dp))
        }
        items(tools) { DashboardTile(it) { onOpen(it.route) } }
    }
}
