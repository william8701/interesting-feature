package services

import models.Entities.{FeatureEntity, UserEntity}
import models.exception.{FeatureNotFound, UserNotFound}
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{AsyncWordSpec, MustMatchers}
import org.scalatestplus.mockito.MockitoSugar
import repositories.{FeatureRepository, UserFeatureEnabledRepository, UserRepository}

import scala.concurrent.Future

class FeatureServiceSpec extends AsyncWordSpec with MustMatchers with MockitoSugar with ScalaFutures {

  private val featureRepository = mock[FeatureRepository]
  private val userRepository = mock[UserRepository]
  private val userFeatureEnabledRepository = mock[UserFeatureEnabledRepository]

  private val sampleEmail = "email@email.com"
  private val sampleFeatureName = "SOME_FEATURE_NAME"
  private val sampleUserEntity = UserEntity(Some(1), sampleEmail)
  private val sampleFeatureEntity = FeatureEntity(Some(1), sampleFeatureName)

  private val featureService = new FeatureService(featureRepository, userRepository, userFeatureEnabledRepository)

  "FeatureService#isFeatureEnabled" should {
    "return true if feature is enabled" in {
      when(userRepository.findByEmail(anyString())) thenReturn Future(Some(sampleUserEntity))
      when(featureRepository.get(anyString())) thenReturn Future(Some(sampleFeatureEntity))
      when(userFeatureEnabledRepository.enabled(1, 1)) thenReturn Future(true)
      featureService.isFeatureEnabled(sampleEmail, sampleFeatureName) map (_ mustBe true)
    }
    "return false if feature is not enabled" in {
      when(userRepository.findByEmail(anyString())) thenReturn Future(Some(sampleUserEntity))
      when(featureRepository.get(anyString())) thenReturn Future(Some(sampleFeatureEntity))
      when(userFeatureEnabledRepository.enabled(1, 1)) thenReturn Future(false)
      featureService.isFeatureEnabled(sampleEmail, sampleFeatureName) map (_ mustBe false)
    }

    "return UserNotFound" in {
      when(userRepository.findByEmail(anyString())) thenReturn Future(Option.empty)
      when(featureRepository.get(anyString())) thenReturn Future(Some(sampleFeatureEntity))
      featureService.isFeatureEnabled(sampleEmail, sampleFeatureName).failed map (_ mustBe a [UserNotFound])
    }

    "return FeatureNotFound" in {
      when(userRepository.findByEmail(anyString())) thenReturn Future(Some(sampleUserEntity))
      when(featureRepository.get(anyString())) thenReturn Future(Option.empty)
      featureService.isFeatureEnabled(sampleEmail, sampleFeatureName).failed map (_ mustBe a [FeatureNotFound])
    }

  }
}
