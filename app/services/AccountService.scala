package services

import java.sql.SQLException

import com.google.common.io.BaseEncoding
import com.google.inject.{ImplementedBy, Inject, Singleton}
import daos._
import entities._
import entities.throwables.NotFoundThrowable
import org.joda.time.DateTime
import sangria.execution.UserFacingError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[AccountService])
trait AccountServiceTrait {
  def create(email: String, companyId, firstName: String, lastName: String): Future[Account]

  def update(id: String, maybeFirstName: Option[String], maybeLastName: Option[String], maybeEmail: Option[String]): Future[Account]
}

@Singleton
class AccountService @Inject()(accountsDAO: AccountsDAOTrait, companiesDAO: CompaniesDAOTrait) extends AccountServiceTrait {

  private def create(email: String, companyId: String, firstName: String, lastName: String): Future[Account] = {
    val account = Account(
      id = java.util.UUID.randomUUID.toString,
      email = email,
      companyId = companyId,
      firstName = firstName,
      lastName = lastName,
      createdAt = DateTime.now,
      updatedAt = DateTime.now
    )

    companiesDAO.getById(companyId).flatMap {
      // specified company not found
      case (None) => Future.failed(new NotFoundThrowable("Company", id))
      case (Some(existingCompany)) =>
        for {
          _ <- accountsDAO.insert(account)
          insertedAccount <- accountsDAO.getById(account.id).map(_.get)
        } yield insertedAccount
    }
  }


  override def update(id: String, maybeFirstName: Option[String], maybeLastName: Option[String], maybeEmail: Option[String]) = {

    accountsDAO.getById(id).flatMap {
      case (None) => Future.failed(new NotFoundThrowable("Account", id))
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