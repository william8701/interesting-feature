package services

import models.Entities.{FeatureEntity, UserEntity}
import models.exception.{FeatureNotFound, UserNotFound}
import repositories.{FeatureRepository, UserFeatureEnabledRepository, UserRepository}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class FeatureService @Inject()(featureRepository: FeatureRepository,
                               userRepository: UserRepository,
                               userFeatureEnabledRepository: UserFeatureEnabledRepository) {

  private def withUserDataAndFeatureData[T](email: String, featureName: String)(f: (UserEntity, FeatureEntity) => Future[T]): Future[T] = {
    val maybeUserAndMaybeFeature = for {
      user <- userRepository.findByEmail(email)
      feature <- featureRepository.get(featureName)
    } yield (user, feature)

    maybeUserAndMaybeFeature flatMap {
      case (Some(user), Some(feature)) => f(user, feature)
      case (None, _) => Future.failed(UserNotFound(email))
      case (_, None) => Future.failed(FeatureNotFound(featureName))
    }
  }

  def isFeatureEnabled(email: String, featureName: String): Future[Boolean] =
    withUserDataAndFeatureData(email, featureName) { (u, f) =>
      userFeatureEnabledRepository.enabled(u.id.get, f.id.get)
    }

  def update(email: String, featureName: String, enabled: Boolean): Future[Boolean] =
    withUserDataAndFeatureData(email, featureName) { (user, feature) =>
      userFeatureEnabledRepository.update(user.id.get, feature.id.get, enabled)
    }
}
