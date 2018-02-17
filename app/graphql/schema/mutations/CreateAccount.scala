package graphql.schema.mutations

import graphql.QueryContext
import entities.Account
import sangria.schema.Context
import sangria.schema.{Argument, BooleanType, IntType, ListInputType, ListType, OptionInputType, StringType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CreateAccount {

  def mutate(ctx: Context[QueryContext, Unit]): Future[Account] = {
    ctx.ctx.services.accountService.insert(
      companyId = ctx.arg(Argument("accountId", StringType)),
      email = ctx.arg(Argument("email", StringType)),
      firstName = ctx.arg(Argument("firstName", StringType)),
      lastName = ctx.arg(Argument("lastName", StringType))
    )
  }
}
