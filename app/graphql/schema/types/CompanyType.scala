package graphql.schema.types

import graphql._
import graphql.schema.Fetchers
import entities.Company
import sangria.schema.{Field, ListType, _}

import scala.concurrent.ExecutionContext.Implicits.global


object CompanyType extends DefinitionTrait[QueryContext, Company] {
  override def definition() = ObjectType(
    "Company",
    "A company",
    () => fields[QueryContext, Company](
      Field("id", StringType, Some("the company's UUID"), resolve = _.value.id),
      Field("name", StringType, Some("the company's name"), resolve = _.value.name),
      Field("accounts", ListType(AccountType), None, arguments = List(SortByArg, OrderArg), resolve = ctx => ctx.ctx.daos.sql.accounts.getByCompanyId(ctx.value.id))
    )
  )
}
