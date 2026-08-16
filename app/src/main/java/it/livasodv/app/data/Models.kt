package it.livasodv.app.data

enum class AppArea(
    val title: String,
    val subtitle: String
) {
    DIRETTIVO("Direttivo", "Gestione"),
    SOCI("Soci", "Turni e servizi"),
    MAGAZZINO("Magazzino", "Materiale e DPI"),
    SERVIZI("Servizi sociali", "Richieste"),
    OLP("S.C. • OLP", "Gestione"),
    OPERATORI("S.C. operatori", "Turni e corsi"),
    CITTADINI("Cittadini", "Richieste"),
    EMERGENZE("Emergenze", "FAQ e primo soccorso"),
    PASSATEMPO("Passatempo", "Rescue Run"),
    MONITOR_PS("Monitor PS 118", "Pronto Soccorso Sardegna"),
    PROTEZIONE_CIVILE("Protezione civile", "Allerte meteo e incendi")
}
