package entities

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.{Json, Writes}

case class Account(id: String, email: String, companyId: String, firstName: String, lastName: String, createdAt: DateTime, updatedAt: DateTime) {
  val fullName = s"$firstName $lastName"
}

object Account {
  implicit val writes: Writes[Account] = new Writes[Account] {
    override def writes(o: Account) = Json.obj(
      "id" -> o.id,
      "email" -> o.email,
      "firstName" -> o.firstName,
      "lastName" -> o.lastName,
      "createdAt" -> o.createdAt.withZone(DateTimeZone.UTC).toString,
      "updatedAt" -> o.updatedAt.withZone(DateTimeZone.UTC).toString,
      "companyId" -> o.companyId
    )
  }
}