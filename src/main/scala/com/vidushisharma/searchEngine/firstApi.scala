package com.vidushisharma.searchEngine
import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.vidushisharma.searchEngine.Models1.{InputData, ServiceJsonProtoocol}
import java.io.File
import java.io.PrintWriter
import com.vidushisharma.searchEngine.Indexer
import scala.io.StdIn
import java.util.Calendar
class first extends Actor{
  def receive ={
    case msg:String=>{
      println(msg+"")
      val writer = new PrintWriter(new File("src/main/resources/"+Calendar.getInstance().getTime()+".txt"))
      writer.write(""+msg)
      writer.close()
      //Thread.sleep(10000)
      println("Done writing to file")


    }
  }
}

object firstApi {
  def main(args: Array[String]) {

    implicit val actorSystem = ActorSystem("MyApi")
    implicit val actor=actorSystem.actorOf(Props[first],"Actor")
    implicit val actorMaterializer = ActorMaterializer()
    implicit val executionContext = actorSystem.dispatcher
    import ServiceJsonProtoocol.inputProtocol
    var name:String=null
    var speciality:String=null
    var fees:String=null
    var location:String=null
    var fullString :String=null

    val route =
      path("doctor") {
        post {
          entity(as[InputData]) {

            doctor => complete {
              name=doctor.name
              speciality=doctor.speciality
              fees=doctor.fees
              location=doctor.location
             // fullString="Name :"+name+"\nSpeciality :"+speciality+"\nFees :"+fees+"\nLocation :"+location
              s"Received Doctor with name ${doctor.name} , speciality : ${doctor.speciality} , fees : ${doctor.fees} and Location ${doctor.location} "

            }
          }
        } ~
          get {
            complete {
              //actor!fullString
              val pt=new Indexer(name , location,fees,speciality)
              pt.Indexer()
              "Name : "+name +" Speciality :"+speciality+" Fees :"+fees+" Location :"+location



            }
          }
      }
//    actor!fullString

    val bindingFuture=Http().bindAndHandle(route, "localhost", 8080)
    println("Server started at 8080")
    //val pt=new Indexer()
    //pt.Indexer()


    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())

  }
}