package v1.feature

import error.ErrorHandlingAction
import models.exception.{FeatureNotFound, UserNotFound}
import models.{FeatureRequest, FeatureResponse}
import org.mockito.ArgumentMatchers.{anyBoolean, anyString}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.mvc.{BodyParsers, Result, Results}
import play.api.test.Helpers.{contentAsString, status, _}
import play.api.test._
import services.FeatureService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.postfixOps

class FeatureControllerTest extends PlaySpec with Results with MockitoSugar {

  private val featureService = mock[FeatureService]
  private val errorHandlingAction = mock[ErrorHandlingAction]
  private val parsers = mock[BodyParsers.Default]

  private val sampleEmail = "email@email.com"
  private val sampleFeatureName = "SOME_FEATURE_NAME"
  val controller = new FeatureController(stubControllerComponents(), new ErrorHandlingAction(parsers), featureService)

  "GET /feature" should {
    "return true" in {
      when(featureService.isFeatureEnabled(anyString(), anyString())) thenReturn Future(true)

      val result: Future[Result] = controller.index(sampleEmail, sampleEmail).apply(FakeRequest())
      val res = Json.fromJson[FeatureResponse](contentAsJson(result))
      status(result) mustBe OK
      res.get mustBe FeatureResponse(true)
    }

    "return false" in {
      when(featureService.isFeatureEnabled(anyString(), anyString())) thenReturn Future(false)
      val result: Future[Result] = controller.index(sampleEmail, sampleEmail).apply(FakeRequest())
      status(result) mustBe OK
      val res = Json.fromJson[FeatureResponse](contentAsJson(result))
      res.get mustBe FeatureResponse(false)
    }

    "return 404 if email not found" in {
      when(featureService.isFeatureEnabled(anyString(), anyString())) thenReturn Future.failed(UserNotFound(sampleEmail))
      val result: Future[Result] = controller.index(sampleEmail, sampleEmail).apply(FakeRequest())
      status(result) mustBe NOT_FOUND
      contentAsString(result) mustBe ""
    }

    "return 404 if feature not found" in {
      when(featureService.isFeatureEnabled(anyString(), anyString())) thenReturn Future.failed(FeatureNotFound(sampleFeatureName))
      val result: Future[Result] = controller.index(sampleEmail, sampleEmail).apply(FakeRequest())
      status(result) mustBe NOT_FOUND
      contentAsString(result) mustBe ""
    }
  }

  "POST /feature" should {
    "return 200 if updated" in {
      when(featureService.update(anyString(), anyString(), anyBoolean())) thenReturn Future(true)
      val result: Future[Result] = controller.update().apply(FakeRequest().withBody(FeatureRequest(sampleFeatureName, sampleEmail, enabled = true)))
      status(result) mustBe OK
      contentAsString(result) mustBe ""
    }

    "return 304 if not updated" in {
      when(featureService.update(anyString(), anyString(), anyBoolean())) thenReturn Future(false)
      val result: Future[Result] = controller.update().apply(FakeRequest().withBody(FeatureRequest(sampleFeatureName, sampleEmail, enabled = true)))
      status(result) mustBe NOT_MODIFIED
      contentAsString(result) mustBe ""
    }

  }
}
