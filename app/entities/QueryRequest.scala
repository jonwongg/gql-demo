package entities

import play.api.libs.functional.syntax._
import play.api.libs.json.{JsObject, JsPath, Json, Reads}

case class QueryRequest(query: String, operationName: Option[String], variables: Option[JsObject])

object QueryRequest {
  private def parseVariables(variables: String) = {
    if (variables.trim == "" || variables.trim == "null") {
      Json.obj()
    }
    else {
      Json.parse(variables).as[JsObject]
    }
  }

  implicit val reads: Reads[QueryRequest] = (
    (JsPath \ "query").read[String] and
    (JsPath \ "operationName").readNullable[String] and
    (JsPath \ "variables").readNullable[JsObject].or((JsPath \ "variables").readNullable[String].map(_.map(parseVariables)))
  ) (QueryRequest.apply _)

}
