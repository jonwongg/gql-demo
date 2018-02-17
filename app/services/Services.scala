package services

import com.google.inject.{ImplementedBy, Inject, Singleton}

@ImplementedBy(classOf[Services])
trait ServicesTrait {
  val accountService: AccountServiceTrait
  val companyService: CompanyServiceTrait
}

@Singleton
class Services @Inject()(val accountService: AccountServiceTrait, val companyService: CompanyServiceTrait) extends ServicesTrait
