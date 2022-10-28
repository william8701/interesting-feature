package repositories

import models.Models.FeatureData

import scala.concurrent.Future

trait FeatureRepository {
  def create(data: FeatureData): Future[Int]

  def list(): Future[Seq[FeatureData]]

  def getByEmail(email: String): Future[Seq[FeatureData]]

  def get(id: Int): Future[Option[FeatureData]]

  def getByEmailAndName(email: String, name: String): Future[Option[FeatureData]]
}

class FeatureRepositoryImpl extends FeatureRepository {
  override def create(data: FeatureData): Future[Int] = ???

  override def list(): Future[Seq[FeatureData]] = ???

  override def get(id: Int): Future[Option[FeatureData]] = ???

  override def getByEmail(email: String): Future[Seq[FeatureData]] = ???

  override def getByEmailAndName(email: String, name: String): Future[Option[FeatureData]] = ???
}
