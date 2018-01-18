package com.vidushisharma.searchEngine

import spray.json.DefaultJsonProtocol

object Models1 {
  case class InputData(name: String, speciality:String , fees: String , location: String )

  object ServiceJsonProtoocol extends DefaultJsonProtocol {
    implicit val inputProtocol = jsonFormat4(InputData)// Because 4 Fields
  }
}
