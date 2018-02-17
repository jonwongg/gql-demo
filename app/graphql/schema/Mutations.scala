package graphql.schema

import graphql.schema.mutations._
import graphql.schema.types._
import graphql._
import sangria.schema.{Field, _}
import sangria.schema.{Argument, BooleanType, IntType, ListInputType, ListType, OptionInputType, StringType}

import scala.collection.immutable.Seq

object Mutations {

  def Mutations() = {
    ObjectType(
      "Mutation",
      {
        fields[QueryContext, Unit](
          Field("createCompany", CompanyType.definition, arguments = List(Argument("name", StringType)), resolve = ctx => CreateCompany.mutate(ctx)),
          Field("createAccount", AccountType.definition, arguments = List(Argument("email", StringType), Argument("companyId", StringType), Argument("firstName", StringType), Argument("lastName", StringType)), resolve = ctx => CreateAccount.mutate(ctx)),
          Field("updateCompany", CompanyType.definition, arguments = List(Argument("id", StringType), Argument("name", OptionInputType(StringType))), resolve = ctx => UpdateCompany.mutate(ctx)),
          Field("updateAccount", AccountType.definition, arguments = List(Argument("id", StringType), Argument("firstName", OptionInputType(StringType)), Argument("lastName", OptionInputType(StringType)), Argument("email", OptionInputType(StringType))), resolve = ctx => UpdateAccount.mutate(ctx))
        )
      }
    )
  }
}
