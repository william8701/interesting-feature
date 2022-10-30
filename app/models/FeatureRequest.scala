package models

import play.api.libs.json.{Json, OFormat}

case class FeatureRequest(featureName: String, email: String, enabled: Boolean)

object FeatureRequest {
  implicit val featureRequestJson: OFormat[FeatureRequest] = Json.format[FeatureRequest]
}
