package graphql.schema.types

import graphql._
import graphql.schema.Fetchers
import graphql.schema.scalars.DateTimeType
import entities._
import sangria.schema.{Field, ListType, OptionType, _}
import utils.SortableHelper._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AccountType extends DefinitionTrait[QueryContext, Account] {

  override def definition() = ObjectType(
    "Account",
    "An account",
    () => {
      Field("id", StringType, Some("String UUID for this account"), resolve = _.value.id),
      Field("email", StringType, None, resolve = _.value.email),
      Field("firstName", StringType, None, resolve = ctx => ctx.value.firstName),
      Field("lastName", StringType, None, resolve = _.value.lastName),
      Field("createdAt", StringType, resolve = _.value.createdAt.toString),
      Field("updatedAt", StringType, resolve = _.value.updatedAt.toString),
      Field("company", CompanyType, Some("The entity that owns this account"), resolve = ctx => Fetchers.companies.defer(ctx.value.companyId))
    }
  )
}
