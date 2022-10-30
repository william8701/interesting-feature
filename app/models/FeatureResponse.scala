package models

import play.api.libs.json.{Json, OFormat}

case class FeatureResponse(canAccess: Boolean)

object FeatureResponse {
  implicit val featureResponseJson: OFormat[FeatureResponse] = Json.format[FeatureResponse]
}