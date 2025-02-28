/*
 * Copyright 2022 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package testUtils

import auth.MtdItUser
import config.{FrontendAppConfig, ItvcHeaderCarrierForPartialsConverter}
import controllers.agent.utils
import implicits.ImplicitDateFormatterImpl
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.scalatest.{BeforeAndAfterEach, Suite}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.http.HeaderNames
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}
import play.api.{Configuration, Environment}
import testConstants.BaseTestConstants._
import testConstants.IncomeSourceDetailsTestConstants._
import uk.gov.hmrc.auth.core.retrieve.Name
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}
import uk.gov.hmrc.play.language.LanguageUtils
import uk.gov.hmrc.play.partials.HeaderCarrierForPartials

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait TestSupport extends UnitSpec with GuiceOneAppPerSuite with MockitoSugar with BeforeAndAfterEach with MaterializerSupport with Injecting {
  this: Suite =>

  import org.scalactic.Equality
  import play.twirl.api.Html

  implicit val htmlEq =
    new Equality[Html] {
      def areEqual(a: Html, b: Any): Boolean = {
        Jsoup.parse(a.toString()).text() == Jsoup.parse(b.toString()).text()
      }
    }

  implicit val timeout: PatienceConfig = PatienceConfig(5.seconds)

  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  val languageUtils: LanguageUtils = app.injector.instanceOf[LanguageUtils]

  implicit val mockImplicitDateFormatter: ImplicitDateFormatterImpl = new ImplicitDateFormatterImpl(languageUtils)

  implicit val messages: Messages = app.injector.instanceOf[MessagesApi].preferred(FakeRequest())

  def messagesApi: MessagesApi = inject[MessagesApi]

  implicit val mockItvcHeaderCarrierForPartialsConverter: ItvcHeaderCarrierForPartialsConverter = mock[ItvcHeaderCarrierForPartialsConverter]

  implicit val headerCarrier: HeaderCarrier = HeaderCarrier().withExtraHeaders(HeaderNames.REFERER -> testReferrerUrl)
  implicit val hcwc: HeaderCarrierForPartials = HeaderCarrierForPartials(headerCarrier)

  implicit val conf: Configuration = app.configuration
  implicit val environment: Environment = app.injector.instanceOf[Environment]
  implicit val appConfig: FrontendAppConfig = app.injector.instanceOf[FrontendAppConfig]

  implicit val individualUser: MtdItUser[_] = MtdItUser(
    mtditid = testMtditid,
    nino = testNino,
    userName = Some(testRetrievedUserName),
    incomeSources = businessAndPropertyAligned,
    btaNavPartial = None,
    saUtr = Some(testSaUtr),
    credId = Some(testCredId),
    userType = Some(testUserTypeIndividual),
    arn = None
  )(FakeRequest())

  implicit val serviceInfo: Html = Html("")

  implicit class JsoupParse(x: Future[Result]) {
    def toHtmlDocument: Document = Jsoup.parse(contentAsString(x))
  }

  lazy val fakeRequestWithActiveSession: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    SessionKeys.lastRequestTimestamp -> "1498236506662",
    SessionKeys.authToken -> "Bearer Token"
  ).withHeaders(
    HeaderNames.REFERER -> "/test/url"
  )

  def fakeRequestWithActiveSessionWithReferer(referer: String): FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    SessionKeys.lastRequestTimestamp -> "1498236506662",
    SessionKeys.authToken -> "Bearer Token"
  ).withHeaders(
    HeaderNames.REFERER -> referer
  )

  lazy val fakeRequestWithTimeoutSession: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    SessionKeys.lastRequestTimestamp -> "1498236506662"
  )

  lazy val fakeRequestWithClientUTR: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    utils.SessionKeys.clientUTR -> "1234567890"
  )

  lazy val fakeRequestWithActiveAndRefererToHomePage: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    SessionKeys.lastRequestTimestamp -> "1498236506662",
    SessionKeys.authToken -> "Bearer Token"
  ).withHeaders(
    HeaderNames.REFERER -> "http://www.somedomain.org/report-quarterly/income-and-expenses/view"
  )

  lazy val fakeRequestWithClientDetails: FakeRequest[AnyContentAsEmpty.type] = fakeRequestWithActiveSession.withSession(
    utils.SessionKeys.clientFirstName -> "Test",
    utils.SessionKeys.clientLastName -> "User",
    utils.SessionKeys.clientUTR -> "1234567890",
    utils.SessionKeys.clientMTDID -> "XAIT00000000015"
  )

  def fakeRequestConfirmedClient(clientNino: String = "AA111111A"): FakeRequest[AnyContentAsEmpty.type] =
    fakeRequestWithActiveSession.withSession(
      utils.SessionKeys.clientFirstName -> "Test",
      utils.SessionKeys.clientLastName -> "User",
      utils.SessionKeys.clientUTR -> "1234567890",
      utils.SessionKeys.clientMTDID -> "XAIT00000000015",
      utils.SessionKeys.clientNino -> clientNino,
      utils.SessionKeys.confirmedClient -> "true"
    )

  def fakeRequestConfirmedClientWithCalculationId(clientNino: String = "AA111111A"): FakeRequest[AnyContentAsEmpty.type] =
    fakeRequestWithActiveSession.withSession(
      utils.SessionKeys.clientFirstName -> "Test",
      utils.SessionKeys.clientLastName -> "User",
      utils.SessionKeys.clientUTR -> "1234567890",
      utils.SessionKeys.clientMTDID -> testMtditid,
      utils.SessionKeys.clientNino -> clientNino,
      utils.SessionKeys.confirmedClient -> "true",
      forms.utils.SessionKeys.calculationId -> "1234567890"
    )

  def fakeRequestConfirmedClientWithReferer(clientNino: String = "AA111111A", referer: String): FakeRequest[AnyContentAsEmpty.type] =
    fakeRequestWithActiveSession.withSession(
      utils.SessionKeys.clientFirstName -> "Test",
      utils.SessionKeys.clientLastName -> "User",
      utils.SessionKeys.clientUTR -> "1234567890",
      utils.SessionKeys.clientMTDID -> "XAIT00000000015",
      utils.SessionKeys.clientNino -> clientNino,
      utils.SessionKeys.confirmedClient -> "true"
    ).withHeaders(
      HeaderNames.REFERER -> referer
    )

  def agentUserConfirmedClient(clientNino: String = "AA111111A"): MtdItUser[_] = MtdItUser(
    mtditid = "XAIT00000000015",
    nino = clientNino,
    userName = Some(Name(Some("Test"), Some("User"))),
    incomeSources = businessesAndPropertyIncome,
    btaNavPartial = None,
    saUtr = Some("1234567890"),
    credId = Some(testCredId),
    userType = Some(testUserTypeAgent),
    arn = Some(testArn)
  )(FakeRequest())

  lazy val fakeRequestWithNino: FakeRequest[AnyContentAsEmpty.type] = fakeRequestWithActiveSession.withSession("nino" -> testNino)
  def fakeRequestWithNinoAndOrigin(origin: String): FakeRequest[AnyContentAsEmpty.type] = fakeRequestWithActiveSession.withSession("nino" -> testNino,
    "origin" -> origin)

  lazy val fakeRequestWithNinoAndCalc: FakeRequest[AnyContentAsEmpty.type] = fakeRequestWithActiveSession.withSession(
    forms.utils.SessionKeys.calculationId -> "1234567890",
    "nino" -> testNino
  )

  lazy val fakeRequestNoSession: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()

  implicit class FakeRequestUtil[C](fakeRequest: FakeRequest[C]) {
    def addingToSession(newSessions: (String, String)*): FakeRequest[C] = {
      fakeRequest.withSession(fakeRequest.session.data ++: newSessions: _*)
    }
  }

}
