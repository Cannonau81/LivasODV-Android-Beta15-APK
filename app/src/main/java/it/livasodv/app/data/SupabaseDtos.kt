package it.livasodv.app.data
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable data class MemberRow(val id:String,@SerialName("first_name") val firstName:String="",@SerialName("last_name") val lastName:String="",val phone:String?=null,val email:String?=null,val role:String?=null,val notes:String?=null)
@Serializable data class VehicleRow(val id:String,val name:String="",@SerialName("make_model") val makeModel:String?=null,@SerialName("license_plate") val licensePlate:String?=null,val notes:String?=null)
@Serializable data class WarehouseRow(val id:String,val name:String="",val category:String?=null,val quantity:Int=0,val notes:String?=null)
@Serializable data class ShiftRow(val id:String,val title:String="",val date:String="",@SerialName("start_time") val start:String?=null,@SerialName("end_time") val end:String?=null,val notes:String?=null)
@Serializable data class ServiceRow(val id:String,val title:String="",val date:String="",@SerialName("from_place") val fromPlace:String?=null,@SerialName("to_place") val toPlace:String?=null,val notes:String?=null)
@Serializable data class CommunicationRow(val id:String,val title:String="",val body:String="",val date:String="",val urgent:Boolean=false)
