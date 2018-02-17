package services

import com.google.inject.{ImplementedBy, Inject, Singleton}
import controllers.AuthenticatedRequest
import daos.DAOsTrait
import graphql._
import graphql.schema.Fetchers
import entities.QueryRequest
import org.parboiled2.Position
import play.api.libs.json._
import sangria.execution.ExecutionScheme.Extended
import sangria.execution.{Executor, QueryAnalysisError, RegisteredError}
import sangria.parser.QueryParser
import sangria.schema.Schema

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@ImplementedBy(classOf[GraphQLQueryService])
trait GraphQLQueryServiceTrait {
  def query(authRequest: AuthenticatedRequest[JsValue], queryRequest: QueryRequest): Future[Either[Throwable, JsValue]]
}

@Singleton
class GraphQLQueryService @Inject()(daos: DAOsTrait, services: ServicesTrait)(implicit exec: ExecutionContext) extends GraphQLQueryServiceTrait{

  override def query(authRequest: AuthenticatedRequest[JsValue], queryRequest: QueryRequest): Future[Either[Throwable, JsValue]] = {
    QueryParser.parse(queryRequest.query) match {
      case Failure(throwable) => Future.successful(Left(throwable))
      case Success(document) =>
        val futureResult = Executor.execute(
          schema = sangria.schema.Schema(Queries.Queries(), Some(Mutations.Mutations())),
          queryAst = document,
          operationName = queryRequest.operationName,
          variables = queryRequest.variables.getOrElse(Json.obj()),
          userContext = QueryContext(daos, services, authRequest),
          deferredResolver = Fetchers.resolver,
          maxQueryDepth = Some(10)
        )

        futureResult.map {
          case result if result.errors.isEmpty => Right(Json.toJson(result.result))
          case result => Left(CustomError(result.errors.head))
        }.recover {
          case throwable: QueryAnalysisError => Left(throwable)
        }
    }
  }
}

case class CustomError(message: String, path: Seq[String], location: Position) extends Throwable(message)
object CustomError {
  def apply(error: RegisteredError): CustomError = new CustomError(error.error.getMessage, error.path.path.map(_.toString), error.position.get)
}