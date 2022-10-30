package models.exception

sealed abstract class NotFoundException(message: String) extends RuntimeException(message)

case class FeatureNotFound(name: String) extends NotFoundException(s"Feature [$name] not found")

case class UserNotFound(email: String) extends NotFoundException(s"User [$email] not found")

case class ValidationError(message: String) extends RuntimeException(message)
