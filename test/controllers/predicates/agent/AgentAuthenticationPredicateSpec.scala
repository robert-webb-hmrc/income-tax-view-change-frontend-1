/*
 * Copyright 2021 HM Revenue & Customs
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

package controllers.predicates.agent

import assets.BaseTestConstants._
import controllers.predicates.agent.AgentAuthenticationPredicate._
import controllers.predicates.agent.AuthPredicate.AuthPredicateSuccess
import org.scalatest.EitherValues
import org.scalatest.MustMatchers.convertToAnyMustWrapper
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import testUtils.TestSupport
import uk.gov.hmrc.auth.core.{AffinityGroup, ConfidenceLevel, Enrolment, Enrolments}
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.http.SessionKeys.{authToken, lastRequestTimestamp}



class AgentAuthenticationPredicateSpec extends TestSupport with MockitoSugar with ScalaFutures with EitherValues {

  private def testUser(affinityGroup: Option[AffinityGroup],
                       confidenceLevel: ConfidenceLevel,
                       enrolments: Enrolment*): IncomeTaxAgentUser = IncomeTaxAgentUser(
    enrolments = Enrolments(enrolments.toSet),
    affinityGroup = affinityGroup,
    confidenceLevel: ConfidenceLevel,
  )

  private def testUser(affinityGroup: Option[AffinityGroup],enrolments: Enrolment*): IncomeTaxAgentUser =
    testUser(affinityGroup,testConfidenceLevel,enrolments: _*)

  lazy val userMatchingRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(ITSASessionKeys.JourneyStateKey -> AgentUserMatching.name)

  val blankUser: IncomeTaxAgentUser = testUser(None, confidenceLevel = ConfidenceLevel.L0)
  lazy val signUpRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(ITSASessionKeys.JourneyStateKey -> AgentSignUp.name)

  lazy val homelessAuthorisedRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(authToken -> "", lastRequestTimestamp -> "")

  val userWithArnIdEnrolment: IncomeTaxAgentUser = testUser(None, arnEnrolment)

  lazy val authorisedRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest().withSession(
    authToken -> "",
    lastRequestTimestamp -> "",
    ITSASessionKeys.JourneyStateKey -> AgentSignUp.name
  )

  "signUpJourneyPredicate" should {
    "return an AuthPredicateSuccess where a user has the JourneyState flag set to Registration" in {
      signUpJourneyPredicate(signUpRequest)(blankUser).right.value mustBe AuthPredicateSuccess
    }

    "return the index page for any other state" in {
      await(signUpJourneyPredicate(FakeRequest())(blankUser).left.value) mustBe homeRoute
    }
  }

  "notSubmitted" should {
    "return an AuthPredicateSuccess where an arn enrolment does not already exist" in {
      notSubmitted(FakeRequest())(blankUser).right.value mustBe AuthPredicateSuccess
    }

    "return the confirmation page where an arn enrolment already exists" in {
      await(notSubmitted(FakeRequest())(userWithArnIdEnrolment).left.value) mustBe confirmationRoute
    }
  }

  "hasSubmitted" should {
    "return an AuthPredicateSuccess where an arn enrolment already exists" in {
      hasSubmitted(FakeRequest())(userWithArnIdEnrolment).right.value mustBe AuthPredicateSuccess
    }

    "return a NotFoundException where an arn enrolment does not already exist" in {
      intercept[NotFoundException](await(hasSubmitted(FakeRequest())(blankUser).left.value))
    }
  }

  "arnPredicate" should {
    "return an AuthPredicateSuccess where an arn enrolment already exists" in {
      arnPredicate(FakeRequest())(userWithArnIdEnrolment).right.value mustBe AuthPredicateSuccess
    }

    "return a NotFoundException where an arn enrolment does not already exist" in {
      intercept[NotFoundException](await(arnPredicate(FakeRequest())(blankUser).left.value))
    }
  }

  "timeoutPredicate" should {
    "return an AuthPredicateSuccess where the lastRequestTimestamp is not set" in {
      timeoutPredicate(FakeRequest())(blankUser).right.value mustBe AuthPredicateSuccess
    }

    "return an AuthPredicateSuccess where the authToken is set and hte lastRequestTimestamp is set" in {
      timeoutPredicate(authorisedRequest)(blankUser).right.value mustBe AuthPredicateSuccess
    }

    "return the timeout page where the lastRequestTimestamp is set but the auth token is not" in {
      lazy val request = FakeRequest().withSession(lastRequestTimestamp -> "")
      await(timeoutPredicate(request)(blankUser).left.value) mustBe timeoutRoute
    }
  }

  "userMatchingJourneyPredicate" should {
    "return an AuthPredicateSuccess where a user has the JourneyState flag set to Registration" in {
      userMatchingJourneyPredicate(userMatchingRequest)(blankUser).right.value mustBe AuthPredicateSuccess
    }

    "return the index page for any other state" in {
      await(userMatchingJourneyPredicate(FakeRequest())(blankUser).left.value) mustBe homeRoute
    }
  }

}
