package graphql.schema.mutations

import graphql.QueryContext
import graphql.schema.Arguments._
import entities.Account
import sangria.schema.{Context, Argument, BooleanType, IntType, ListInputType, ListType, OptionInputType, StringType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UpdateAccount {

  def mutate(ctx: Context[QueryContext, Unit]): Future[Account] = {
    ctx.ctx.services.accountService.update(
      id = ctx.arg(Argument("id", StringType)),
      maybeFirstName = ctx.arg(Argument("firstName", OptionInputType(StringType))),
      maybeLastName = ctx.arg(Argument("lastName", OptionInputType(StringType))),
      maybeEmail = ctx.arg(Argument("email", OptionInputType(StringType)))
    )
  }
}
