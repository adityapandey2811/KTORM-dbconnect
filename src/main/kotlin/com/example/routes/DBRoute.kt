package com.example.routes

import com.example.entity.DataFormat
import com.example.entity.NotesEntity
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.database.Database
import org.ktorm.dsl.*

fun Application.connectToDB(){
    val databaseConn = Database.connect(
        url = "jdbc:mysql://localhost:3306/notes",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "root"
    )
    routing {
        route("/dbconnect"){
            get{
                val listDB = mutableListOf<DataFormat>()
                val readDB = databaseConn.from(NotesEntity)
                    .select()
                for(row in readDB){
                    listDB.add(DataFormat(row[NotesEntity.id]!!,row[NotesEntity.note]!!))
                }
                call.respond(listDB)
            }
            get("/"){
                val listDB = mutableListOf<DataFormat>()
                val readDB = databaseConn.from(NotesEntity)
                    .select()
                for(row in readDB){
                    listDB.add(DataFormat(row[NotesEntity.id]!!,row[NotesEntity.note]!!))
                }
                call.respond(listDB)
            }
            get("{id?}"){
                val listDB = mutableListOf<DataFormat>()
                val readDB = databaseConn.from(NotesEntity).select()
                val id:String? = call.parameters["id"]
                for (row in readDB){
                    if(row[NotesEntity.id].toString() == id){
                        listDB.add(DataFormat(row[NotesEntity.id]!!,row[NotesEntity.note]!!))
                        call.respond(listDB)
                    }
                }
                call.respondText("Not Found with ID: ${call.parameters["id"]}", status = HttpStatusCode.BadRequest)
            }
            post {
                val customerData = call.receive<DataFormat>()
                databaseConn.insert(NotesEntity){
                    set(it.note, customerData.note)
                }
                call.respondText("Entered Data Successfully")
            }
            post("{id?}"){
                val customerData = call.receive<DataFormat>()
                val id = call.parameters["id"]?.toInt()
                databaseConn.update(NotesEntity){
                    set(it.note,customerData.note)
                    where{
                        it.id.toInt() eq id!!
                    }
                }
                call.respondText("Field Updated", status = HttpStatusCode.OK)
            }
            delete("{id?}") {
                val id = call.parameters["id"]?.toInt()
                databaseConn.delete(NotesEntity){
                    it.id.toInt() eq id!!
                }
                call.respondText("Deleted Successfully", status = HttpStatusCode.OK)
            }
        }
    }
}