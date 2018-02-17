package services

import java.sql.SQLException

import com.google.inject.{ImplementedBy, Inject, Singleton}
import daos.sql._
import graphql.schema.Arguments
import entities._
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[CompanyService])
trait CompanyServiceTrait {
  def create(name: String): Future[Company]
  def update(id: String, name: String): Future[Company]
}

@Singleton
class CompanyService @Inject()(companiesDAO: CompaniesDAOTrait) extends CompanyServiceTrait {

  override def create(name: String) = {
    val company = Company(
      id = java.util.UUID.randomUUID.toString,
      name = name
    )

    companiesDAO.insert(company).map(_ => company)
  }

  override def update(id: String, maybeFirstName: Option[String], maybeLastName: Option[String], maybeEmail: Option[String]) = {

    companiesDAO.getById(id).flatMap {
      case (None) => Future.failed(new NotFoundThrowable("Company", id))
      case (Some(existingAccount)) =>
        val newVersion = existingAccount.copy(
          firstName = maybeFirstName.getOrElse(existingAccount.firstName),
          lastName = maybeLastName.getOrElse(existingAccount.lastName),
          email = maybeEmail.getOrElse(existingAccount.email)
        )

        for {
          _ <- accountsDAO.update(newVersion).map(_ => newVersion)
          // Return the updated account
          updatedAccount = newVersion
        } yield updatedAccount
    }
  }
}
