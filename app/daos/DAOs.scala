package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}

@ImplementedBy(classOf[DAOs])
trait DAOsTrait {
  val sql: SQLDAOsTrait
}

@Singleton
class DAOs @Inject()(val sql: SQLDAOsTrait) extends DAOsTrait
