package error

import models.exception.{FeatureNotFound, UserNotFound}
import play.api.mvc.Results.{BadRequest, NotFound}
import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ErrorHandlingAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext) extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: Request[A] => Future[Result]): Future[Result] =
    block(request).recover(handlerError)

  def handlerError: PartialFunction[Throwable, Result] = {
    case FeatureNotFound(_) => NotFound
    case UserNotFound(_) => NotFound
    case _ => BadRequest
  }
}
