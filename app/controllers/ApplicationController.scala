package controllers

import javax.inject._

import entities.QueryRequest
import play.api.libs.json._
import play.api.mvc.Action
import sangria.execution.{ErrorWithResolver, QueryAnalysisError}
import sangria.marshalling.playJson._
import sangria.parser.SyntaxError
import services.GraphQLQueryServiceTrait

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(graphQLQueryService: GraphQLQueryServiceTrait)(implicit exec: ExecutionContext) extends ControllerWithAuthentication() {

  def graphiql = Action {
    Ok(views.html.graphiql())
  }

  def query = AuthenticatedAction.async(parse.json) { request =>
    request.body.validate[QueryRequest].fold(
      errors => Future.successful(BadRequest(JsError.toJson(errors))),
      queryRequest => graphQLQueryService.query(request, queryRequest).map {
        case Right(jsValue) => Ok(jsValue)
        case Left(throwable: SyntaxError) => BadRequest(throwable.getMessage)
        case Left(throwable: QueryAnalysisError) => BadRequest(throwable.resolveError)
        case Left(throwable: ErrorWithResolver) => InternalServerError(throwable.resolveError)
        case Left(throwable) =>
          throwable.printStackTrace()
          InternalServerError("Internal Server Error")
      }
    )
  }
}
