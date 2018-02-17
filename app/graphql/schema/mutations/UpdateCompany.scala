package graphql.schema.mutations

import graphql.QueryContext
import entities._
import sangria.schema.Context
import sangria.schema.{Argument, IntType, StringType}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UpdateCompany {

  def mutate(ctx: Context[QueryContext, Unit]): Future[Company] = {
    ctx.ctx.services.companyService.update(
      id = ctx.arg(Argument("id", IntType)),
      name = ctx.arg(Argument("name", StringType))
    )
  }
}
