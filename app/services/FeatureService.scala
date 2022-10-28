package services


import models.Models.FeatureData
import repositories.FeatureRepository
import v1.feature.FeatureRequest

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class FeatureService @Inject()(val featureRepository: FeatureRepository) {
  def isFeatureEnabled: Future[Boolean] = Future.successful(true)

  def getFeatures(email: String): Future[Seq[FeatureData]] = featureRepository.getByEmail(email)

  def getFeatureByName(email: String, featureName: String): Future[FeatureData] =
    featureRepository.getByEmailAndName(email, featureName)
      .map {
        case Some(feature) =>feature
        case None => throw new Exception //TODO: Handle this exception
      }

  def createFeature(feature: FeatureData): Future[Int] = featureRepository.create(feature)

  def updateFeature(feature: FeatureRequest): Future[FeatureData] = ???

}
