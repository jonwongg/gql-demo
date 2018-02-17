package daos

import com.github.tototoshi.slick.H2JodaSupport._
import com.google.inject.{ImplementedBy, Inject, Singleton}
import entities.Account
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.DBIOAction

import scala.concurrent.Future

@ImplementedBy(classOf[AccountsDAO])
trait AccountsDAOTrait {
  def insert(account: Account): Future[Int]

  def getById(id: String): Future[Option[Account]]

  def getByIds(ids: Seq[String]): Future[Seq[Account]]

  def getByEmail(email: String): Future[Option[Account]]

  def getByCompanyId(companyId: String): Future[Seq[Account]]

  def update(account: Account): Future[Int]
}

@Singleton
class AccountsDAO @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends Tables(dbConfigProvider) with AccountsDAOTrait {

  import driver.api._

  override def insert(account: Account) = db.run(
    accounts.map(a => (a.id, a.email, a.companyID, a.firstName, a.lastName, a.createdAt, a.updatedAt)) +=
      (account.id, account.email, account.companyID, account.firstName, account.lastName, account.createdAt, account.updatedAt)
  )

  override def getById(id: String) = db.run(accounts.filter(_.id === id).result.headOption)

  override def getByIds(ids: Seq[String]): Future[Seq[Account]] = db.run(accounts.filter(_.id inSet ids).result)

  override def getByEmail(email: String) = db.run(accounts.filter(_.email === email).result.headOption)

  override def getByCompanyId(companyId: String) = db.run(accounts.filter(_.companyId === companyId).result)

  override def update(account: Account) = db.run(accounts.filter(_.id === account.id).update(account))
}