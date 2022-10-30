package repositories

import com.google.inject.ImplementedBy
import models.Entities.{UserEnabledFeatureEntity, UserEntity}

import javax.inject.Singleton
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[InMemoryUserFeatureEnabledRepository])
trait UserFeatureEnabledRepository {

  def update(userId: Int, featureId: Int, enabled: Boolean): Future[Boolean]

  def enabled(userId: Int, featureId: Int): Future[Boolean]

  def get(userId: Int, featureId: Int): Future[Option[UserEnabledFeatureEntity]]

}

@Singleton
class InMemoryUserFeatureEnabledRepository extends UserFeatureEnabledRepository {

  val enabledFeatures = new ListBuffer[UserEnabledFeatureEntity]()
  enabledFeatures.addOne(UserEnabledFeatureEntity(1, 1, enabled = true))
  enabledFeatures.addOne(UserEnabledFeatureEntity(2, 1, enabled = true))
  enabledFeatures.addOne(UserEnabledFeatureEntity(3, 1, enabled = false))

  override def update(userId: Int, featureId: Int, enabled: Boolean): Future[Boolean] = Future {
    enabledFeatures.find(e => e.featureId == featureId && e.userId == userId) match {
      case Some(e) if e.enabled == enabled =>
        false
      case Some(e) =>
        e.enabled = enabled
        true
      case None =>
        enabledFeatures.addOne(UserEnabledFeatureEntity(userId, featureId, enabled))
        true
    }
  }

  override def enabled(userId: Int, featureId: Int): Future[Boolean] = Future {
    enabledFeatures.find(e => e.featureId == featureId && e.userId == userId) match {
      case Some(e) => e.enabled
      case None => false
    }
  }

  override def get(userId: Int, featureId: Int): Future[Option[UserEnabledFeatureEntity]] = Future {
    enabledFeatures.find(e => e.featureId == featureId && e.userId == userId)
  }

}
