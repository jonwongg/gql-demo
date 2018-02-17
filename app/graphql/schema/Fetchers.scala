package graphql.schema

import graphql.QueryContext
import sangria.execution.deferred._

object Fetchers {
  // http://sangria-graphql.org/learn/#high-level-fetch-api
  val accounts = Fetcher((ctx: QueryContext, ids: Seq[String]) => ctx.daos.sql.accounts.getByIds(ids))(HasId(_.id))
  val companies = Fetcher((ctx: QueryContext, ids: Seq[String]) => ctx.daos.sql.companies.getByIds(ids))(HasId(_.id))
}
