package v1.feature

import error.ErrorHandlingAction
import models.{FeatureRequest, FeatureResponse}
import play.api.libs.json.Json
import play.api.mvc._
import services.FeatureService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class FeatureController @Inject()(cc: ControllerComponents,
                                  errorHandlingAction: ErrorHandlingAction,
                                  featureService: FeatureService) extends AbstractController(cc) {

  def index(email: String, featureName: String): Action[AnyContent] = errorHandlingAction.async { implicit req: Request[AnyContent] =>
    featureService.isFeatureEnabled(email, featureName) map { enabled =>
      Ok(Json.toJson(FeatureResponse(enabled)))
    }
  }

  def update(): Action[FeatureRequest] = errorHandlingAction(parse.json[FeatureRequest]).async { implicit request: Request[FeatureRequest] =>
    val featureRequest = request.body
    featureService.update(featureRequest.email, featureRequest.featureName, featureRequest.enabled) map {
      case true => Ok
      case false => NotModified
    }
  }

}
