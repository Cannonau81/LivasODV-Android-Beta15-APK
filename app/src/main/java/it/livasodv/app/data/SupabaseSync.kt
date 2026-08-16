package it.livasodv.app.data
import io.github.jan.supabase.postgrest.from

class SupabaseSync(private val repo:LivasRepository=AppGraph.repository){
 suspend fun refreshCore(){
  val db=SupabaseProvider.client
  val members=db.from("members").select().decodeList<MemberRow>().map{Member(it.id,it.firstName,it.lastName,it.phone?:"",it.email?:"",it.role?:"Socio",notes=it.notes?:"")}
  members.forEach(repo::upsertMember)
  val vehicles=db.from("vehicles").select().decodeList<VehicleRow>().map{Vehicle(it.id,it.name,it.makeModel?:"",it.licensePlate?:"",notes=it.notes?:"")}
  vehicles.forEach(repo::upsertVehicle)
  val warehouse=db.from("warehouse_items").select().decodeList<WarehouseRow>().map{WarehouseItem(it.id,it.name,it.category?:"",it.quantity,notes=it.notes?:"")}
  warehouse.forEach(repo::upsertWarehouse)
  val shifts=db.from("shifts").select().decodeList<ShiftRow>().map{Shift(it.id,it.area,it.shiftDate,it.start?:"",it.end?:"",notes=it.notes?:"")}
  shifts.forEach(repo::upsertShift)
  val services=db.from("services").select().decodeList<ServiceRow>().map{Service(it.id,it.title,it.date,it.fromPlace?:"",it.toPlace?:"",notes=it.notes?:"")}
  services.forEach(repo::upsertService)
  val communications=db.from("communications").select().decodeList<CommunicationRow>()
  communications.forEach{repo.upsertCommunication(Communication(it.id,it.title,it.body,it.date,it.urgent))}
 }
}
