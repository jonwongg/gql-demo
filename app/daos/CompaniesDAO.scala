package daos

import com.github.tototoshi.slick.H2JodaSupport._
import com.google.inject.{ImplementedBy, Inject, Singleton}
import entities.Company
import play.api.db.slick.DatabaseConfigProvider
import slick.dbio.Effect.All

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[CompaniesDAO])
trait CompaniesDAOTrait {
  def insert(company: Company): Future[Int]

  def update(company: Company): Future[Int]

  def getById(id: String): Future[Option[Company]]

  def getByIds(ids: Seq[String]): Future[Seq[Company]]

  def all: Future[Seq[Company]]
}

@Singleton
class CompaniesDAO @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider) extends Tables(dbConfigProvider) with CompaniesDAOTrait {

  import driver.api._

  override def insert(company: Company) = db.run(companies.map(c => (c.id, c.name)) +=(company.id, company.name))

  override def update(company: Company) = db.run(companies.filter(_.id === company.id).update(company))

  override def getById(id: String) = db.run(companies.filter(_.id === id).result.headOption)

  override def getByIds(ids: Seq[String]) = db.run(companies.filter(_.id inSet ids).result)

  override def all: Future[Seq[Company]] = db.run(companies.sortBy(_.name).result)

}