package models

object Models {
  case class UserData(id: Int, email: String)

  case class FeatureData(userId: Int, featureName: String, enabled: Boolean)
}

