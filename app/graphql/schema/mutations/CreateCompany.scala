package graphql.schema.mutations

import graphql.QueryContext
import graphql.schema.Arguments._
import sangria.schema.{Argument, BooleanType, IntType, ListInputType, ListType, OptionInputType, StringType}
import graphql.schema.types.CompanyTrialInfoType
import entities.Company
import sangria.schema.Context

import scala.concurrent.Future

object CreateCompany {

  def mutate(ctx: Context[QueryContext, Unit]): Future[Company] = {
    ctx.ctx.services.companyService.create(
      name = ctx.arg(Argument("name", StringType))
    )
  }
}
