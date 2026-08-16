package it.livasodv.app.data
import kotlinx.serialization.Serializable

@Serializable data class Certification(val id:String,val title:String,val issuedAt:String?=null,val expiresAt:String?=null,val issuer:String="",val notes:String="")
@Serializable data class Member(val id:String,val firstName:String,val lastName:String,val phone:String="",val email:String="",val role:String="Socio",val qualifications:List<String> = emptyList(),val enabled118:Boolean=false,val civilProtection:Boolean=false,val aib:Boolean=false,val socialServices:Boolean=false,val driver:Boolean=false,val active:Boolean=true,val notes:String="",val certifications:List<Certification> = emptyList())
@Serializable data class Vehicle(val id:String,val name:String,val makeModel:String="",val licensePlate:String="",val insuranceExpiry:String?=null,val inspectionExpiry:String?=null,val mileage:Int=0,val notes:String="")
@Serializable data class WarehouseItem(val id:String,val name:String,val category:String="",val quantity:Int=0,val minimum:Int=0,val expiry:String?=null,val notes:String="")
@Serializable data class Presidio(val id:String,val name:String,val category:String="",val quantity:Int=0,val available:Boolean=true,val notes:String="")
@Serializable data class Shift(val id:String,val title:String,val date:String,val start:String="",val end:String="",val memberIds:List<String> = emptyList(),val notes:String="")
@Serializable data class Service(val id:String,val title:String,val date:String,val fromPlace:String="",val toPlace:String="",val vehicleId:String?=null,val memberIds:List<String> = emptyList(),val notes:String="")
@Serializable data class Communication(val id:String,val title:String,val body:String,val date:String,val urgent:Boolean=false)
@Serializable data class CitizenRequest(val id:String,val kind:String,val requester:String,val phone:String="",val details:String="",val status:String="Nuova")
@Serializable data class CivilVolunteer(val id:String,val firstName:String,val lastName:String,val active:Boolean=true,val notes:String="")
@Serializable data class CivilRequest(val id:String,val volunteerId:String,val type:String,val fromDate:String,val toDate:String,val status:String="In attesa")
@Serializable data class AuditEvent(val id:String,val date:String,val area:String,val action:String,val detail:String)
@Serializable data class OperationalMission(val id:String,val title:String,val status:String,val location:String="",val vehicle:String="")
@Serializable data class AppNotification(val id:String,val title:String,val body:String,val level:String="info",val read:Boolean=false)
@Serializable data class FullBackup(val generatedAt:String,val version:String,val members:List<Member>,val vehicles:List<Vehicle>,val warehouse:List<WarehouseItem>,val presidi:List<Presidio>,val shifts:List<Shift>,val services:List<Service>,val communications:List<Communication>,val citizenRequests:List<CitizenRequest>,val civilVolunteers:List<CivilVolunteer>,val civilRequests:List<CivilRequest>,val audit:List<AuditEvent>,val missions:List<OperationalMission>)
