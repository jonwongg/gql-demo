package daos

import com.github.tototoshi.slick.H2JodaSupport._
import com.google.inject.Inject
import com.typesafe.slick.driver.ms.SQLServerDriver.api._
import entities._
import org.joda.time.DateTime
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

class Tables @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  protected val accounts = TableQuery[Accounts]
  protected val companies = TableQuery[Companies]

  protected class Accounts(tag: Tag) extends Table[Account](tag, "accounts") {
    def id = column[String]("id", O.PrimaryKey)

    def email = column[String]("email")

    def companyId = column[String]("company_id")

    def firstName = column[String]("first_name")

    def lastName = column[String]("last_name")

    def createdAt = column[DateTime]("created_at")

    def updatedAt = column[DateTime]("updated_at")

    def * = (id, email, companyId, firstName, lastName, createdAt, updatedAt) <> ((Account.apply _).tupled, Account.unapply _)
  }

  protected class Companies(tag: Tag) extends Table[Company](tag, "companies") {
    def id = column[String]("id", O.PrimaryKey)

    def name = column[String]("name")

    def createdAt = column[DateTime]("created_at")

    def updatedAt = column[DateTime]("updated_at")

    def * = (id, name, createdAt, updatedAt) <> ((Company.apply _).tupled, Company.unapply _)
  }

}
