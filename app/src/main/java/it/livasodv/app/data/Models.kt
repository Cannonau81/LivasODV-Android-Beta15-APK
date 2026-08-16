package it.livasodv.app.data
import kotlinx.serialization.Serializable
@Serializable data class Member(val id:String,val nome:String,val cognome:String,val ruolo:String="Socio",val qualifiche:List<String> = emptyList())
@Serializable data class Shift(val id:String,val titolo:String,val data:String,val orario:String,val luogo:String="Gonnosfanadiga")
@Serializable data class Supply(val id:String,val nome:String,val categoria:String,val quantita:Int,val disponibile:Boolean=true,val notes:String="")
enum class AppArea(val title:String,val subtitle:String) {
 DIRETTIVO("Direttivo","Gestione"), SOCI("Soci","Turni e servizi"), MAGAZZINO("Magazzino","Materiale e DPI"),
 SERVIZI("Servizi sociali","Richieste"), OLP("S.C. • OLP","Gestione"), OPERATORI("S.C. operatori","Turni e corsi"),
 CITTADINI("Cittadini","Richieste"), EMERGENZE("Emergenze","FAQ e primo soccorso"), PASSATEMPO("Passatempo","Rescue Run"),
 MONITOR_PS("Monitor PS 118","Pronto Soccorso Sardegna"), PROTEZIONE_CIVILE("Protezione civile","Allerte meteo e incendi")
}
