package it.livasodv.app.data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LivasRepository {
 private val _members=MutableStateFlow<List<Member>>(emptyList()); val members=_members.asStateFlow()
 private val _vehicles=MutableStateFlow<List<Vehicle>>(emptyList()); val vehicles=_vehicles.asStateFlow()
 private val _warehouse=MutableStateFlow<List<WarehouseItem>>(emptyList()); val warehouse=_warehouse.asStateFlow()
 private val _presidi=MutableStateFlow<List<Presidio>>(emptyList()); val presidi=_presidi.asStateFlow()
 private val _shifts=MutableStateFlow<List<Shift>>(emptyList()); val shifts=_shifts.asStateFlow()
 private val _services=MutableStateFlow<List<Service>>(emptyList()); val services=_services.asStateFlow()
 private val _communications=MutableStateFlow<List<Communication>>(emptyList()); val communications=_communications.asStateFlow()
 private val _citizenRequests=MutableStateFlow<List<CitizenRequest>>(emptyList()); val citizenRequests=_citizenRequests.asStateFlow()
 private val _civilVolunteers=MutableStateFlow<List<CivilVolunteer>>(emptyList()); val civilVolunteers=_civilVolunteers.asStateFlow()
 private val _civilRequests=MutableStateFlow<List<CivilRequest>>(emptyList()); val civilRequests=_civilRequests.asStateFlow()
 private val _audit=MutableStateFlow<List<AuditEvent>>(emptyList()); val audit=_audit.asStateFlow()
 private val _missions=MutableStateFlow<List<OperationalMission>>(emptyList()); val missions=_missions.asStateFlow()
 var lastSyncError:String?=null; private set

 fun upsertMember(v:Member){_members.value=_members.value.filterNot{it.id==v.id}+v}
 fun deleteMember(id:String){_members.value=_members.value.filterNot{it.id==id}}
 fun upsertVehicle(v:Vehicle){_vehicles.value=_vehicles.value.filterNot{it.id==v.id}+v}
 fun deleteVehicle(id:String){_vehicles.value=_vehicles.value.filterNot{it.id==id}}
 fun upsertWarehouse(v:WarehouseItem){_warehouse.value=_warehouse.value.filterNot{it.id==v.id}+v}
 fun deleteWarehouse(id:String){_warehouse.value=_warehouse.value.filterNot{it.id==id}}
 fun upsertPresidio(v:Presidio){_presidi.value=_presidi.value.filterNot{it.id==v.id}+v}
 fun deletePresidio(id:String){_presidi.value=_presidi.value.filterNot{it.id==id}}
 fun upsertShift(v:Shift){_shifts.value=_shifts.value.filterNot{it.id==v.id}+v}
 fun upsertService(v:Service){_services.value=_services.value.filterNot{it.id==v.id}+v}
 fun upsertCommunication(v:Communication){_communications.value=_communications.value.filterNot{it.id==v.id}+v}
 fun upsertCitizenRequest(v:CitizenRequest){_citizenRequests.value=_citizenRequests.value.filterNot{it.id==v.id}+v}
 fun upsertCivilVolunteer(v:CivilVolunteer){_civilVolunteers.value=_civilVolunteers.value.filterNot{it.id==v.id}+v}
 fun upsertCivilRequest(v:CivilRequest){_civilRequests.value=_civilRequests.value.filterNot{it.id==v.id}+v}
 fun upsertMission(v:OperationalMission){_missions.value=_missions.value.filterNot{it.id==v.id}+v}
 fun restore(b:FullBackup){_members.value=b.members;_vehicles.value=b.vehicles;_warehouse.value=b.warehouse;_presidi.value=b.presidi;_shifts.value=b.shifts;_services.value=b.services;_communications.value=b.communications;_citizenRequests.value=b.citizenRequests;_civilVolunteers.value=b.civilVolunteers;_civilRequests.value=b.civilRequests;_audit.value=b.audit;_missions.value=b.missions}
}
object AppGraph { val repository=LivasRepository() }
