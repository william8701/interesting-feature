package v1.feature

import play.api.libs.json.Json
import play.api.mvc._
import services.FeatureService

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global

case class FeatureResponse(canAccess: Boolean)

object FeatureResponse {
  implicit val featureResponseJson = Json.format[FeatureResponse]
}

case class FeatureRequest(featureName:String, email: String, enable: Boolean)

object FeatureRequest {
  implicit val featureRequestJson = Json.format[FeatureRequest]
}

@Singleton
class FeatureController @Inject()(cc: ControllerComponents, val featureService: FeatureService) extends AbstractController(cc) {
  /**
   *
   * @param email       user’s email
   * @param featureName the feature name
   * @return Json { "canAccess": true|false }
   */
  def index(email: String, featureName: String): Action[AnyContent] = Action.async { implicit req: Request[AnyContent] =>
    featureService.getFeatureByName(email, featureName)
      .map(feature => Ok(Json.toJson(FeatureResponse(feature.enabled))))
  }


  /**
   * body sample request body { featureName: "AWESOME_FEATURE"，email: "somebody@email.com", enabled: true|false }
   * @return empty response
   */
  def update():Action[FeatureRequest] = Action.async(parse.json[FeatureRequest]) { implicit request: Request[FeatureRequest] =>
    featureService.updateFeature(request.body)
      .map(_ => Ok)
  }
}
