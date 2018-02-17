package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}
import daos._

@ImplementedBy(classOf[SQLDAOs])
trait SQLDAOsTrait {
  val accounts: AccountsDAOTrait
  val companies: CompaniesDAOTrait
}

@Singleton
class SQLDAOs @Inject()(val accounts: AccountsDAOTrait, val companies: CompaniesDAOTrait) extends SQLDAOsTrait