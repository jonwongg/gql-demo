package graphql.schema

import graphql._
import graphql.schema.Arguments._
import graphql.schema.types._
import entities._
import sangria.schema.{Field, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Queries {

  def Queries() = {
    ObjectType(
      "Query",
      () => {
        fields[QueryContext, Unit](
          Field("me", AccountType, resolve = ctx => Fetchers.accounts.defer(ctx.ctx.request.account.id)),
          Field("account", OptionType(AccountType), arguments = List(Argument("id", StringType)), resolve = ctx => Fetchers.accounts.defer(ctx.arg(Argument("id", StringType)))),
          Field("company", OptionType(CompanyType), arguments = List(Argument("id", StringType)), resolve = ctx => Fetchers.companies.defer(ctx.arg(Argument("id", StringType))))
        )
      }
    )
  }
}
