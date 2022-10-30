package repositories

import com.google.inject.ImplementedBy
import models.Entities.FeatureEntity

import javax.inject.Singleton
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@ImplementedBy(classOf[InMemoryFeatureRepository])
trait FeatureRepository {

  def get(id: Int): Future[Option[FeatureEntity]]

  def get(name: String): Future[Option[FeatureEntity]]

}

@Singleton
class InMemoryFeatureRepository extends FeatureRepository {

  val features = new ListBuffer[FeatureEntity]()
  features.addOne(FeatureEntity(Some(1), "FEATURE_ONE"))
  features.addOne(FeatureEntity(Some(2), "FEATURE_TWO"))
  features.addOne(FeatureEntity(Some(3), "FEATURE_THREE"))

  override def get(id: Int): Future[Option[FeatureEntity]] = Future(features.find(d => d.id.get.equals(id)))

  override def get(name: String): Future[Option[FeatureEntity]] = Future(features.find(d => d.featureName.equals(name)))

}
