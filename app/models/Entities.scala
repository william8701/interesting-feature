package models

object Entities {

  case class UserEntity(id: Option[Int], email: String)

  case class FeatureEntity(id: Option[Int], featureName: String)

  case class UserEnabledFeatureEntity(userId: Int, featureId: Int, var enabled: Boolean)

}
