/*
 * Copyright 2017 HM Revenue & Customs
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

package controllers

import helpers.IntegrationTestConstants.{GetBusinessDetails, GetIncomeSourceDetails, GetPropertyDetails, testMtditid, testSelfEmploymentId, testTradeName}
import helpers.servicemocks.IncomeTaxViewChangeStub
import helpers.{ComponentSpecBase, GenericStubMethods}
import play.api.http.Status.{INTERNAL_SERVER_ERROR, OK, SEE_OTHER}

class BusinessDetailsControllerISpec extends ComponentSpecBase with GenericStubMethods {

  "Calling the BusinessDetailsController.getBusinessDetails" when {

    "isAuthorisedUser with an active enrolment and has at least 1 business" should {

      "return the correct page with a valid total" in {
        isAuthorisedUser(true)
        stubUserDetails()

        And("I wiremock stub a successful Income Source Details response with single Business income")
        IncomeTaxViewChangeStub.stubGetIncomeSourceDetailsResponse(testMtditid)(
          OK, GetIncomeSourceDetails.businessAndPropertyResponse(testSelfEmploymentId)
        )

        When("I call GET /report-quarterly/income-and-expenses/view/business-details")
        val res = IncomeTaxViewChangeFrontend.getBusinessDetails(0)

        Then("I verify the Income Source Details has been successfully wiremocked")
        IncomeTaxViewChangeStub.verifyGetIncomeSourceDetails(testMtditid)

        verifyBizObsCall(testSelfEmploymentId)

        Then("the view displays the correct title, username and links")
        res should have(
          httpStatus(OK),
          pageTitle("business"),
          elementTextByID(id = "reporting-period")("Reporting period: 1 January - 31 December"),
          elementTextByID(id = "cessation-date")("This business ceased trading on 31 December 2017."),
          elementTextByID(id = "address-details")("Address and contact details"),
          elementTextByID(id = "trading-name")("Trading name"),
          elementTextByID(id = "trading-name-business")("business"),
          elementTextByID(id = "business-address")("Business address"),
          elementTextByID(id = "address-line-1")("64 Zoo Lane"),
          elementTextByID(id = "address-line-2")("Happy Place"),
          elementTextByID(id = "address-line-3")("Magical Land"),
          elementTextByID(id = "address-line-4")("England"),
          elementTextByID(id = "address-line-5")("ZL1 064"),
          elementTextByID(id = "additional-information")("Additional information"),
          elementTextByID(id = "accounting-method")("This business uses the cash accounting method.")
        )
      }
    }

    "isAuthorisedUser with an active enrolment, but has no business" should {

      "return an internal server error" in {
        isAuthorisedUser(true)
        stubUserDetails()

        And("I wiremock stub a successful Income Source Details response with single Business income")
        IncomeTaxViewChangeStub.stubGetIncomeSourceDetailsResponse(testMtditid)(OK, GetIncomeSourceDetails.propertyOnlyResponse)

        When("I call GET /report-quarterly/income-and-expenses/view/business-details")
        val res = IncomeTaxViewChangeFrontend.getBusinessDetails(0)

        Then("I verify the Income Source Details has been successfully wiremocked")
        IncomeTaxViewChangeStub.verifyGetIncomeSourceDetails(testMtditid)

        verifyBizObsCall(testSelfEmploymentId)

        Then("an ISE is displayed")
        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }
    }

    "isAuthorisedUser with an active enrolment, but the api returns an error response" should {

      "return an internal server error" in {
        isAuthorisedUser(true)
        stubUserDetails()

        And("I wiremock stub a successful Income Source Details response with single Business income")
        IncomeTaxViewChangeStub.stubGetIncomeSourceDetailsResponse(testMtditid)(INTERNAL_SERVER_ERROR, GetIncomeSourceDetails.errorResponse)

        When("I call GET /report-quarterly/income-and-expenses/view/business-details")
        val res = IncomeTaxViewChangeFrontend.getBusinessDetails(0)

        Then("I verify the Income Source Details has been successfully wiremocked")
        IncomeTaxViewChangeStub.verifyGetIncomeSourceDetails(testMtditid)

        Then("an ISE is displayed")
        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }
    }

    "unauthorised" should {

      "redirect to sign in" in {

        isAuthorisedUser(false)

        When("I call GET /report-quarterly/income-and-expenses/view/business-details")
        val res = IncomeTaxViewChangeFrontend.getBusinessDetails(0)

        Then("the http response for an unauthorised user is returned")
        res should have(
          httpStatus(SEE_OTHER),
          redirectURI(controllers.routes.SignInController.signIn().url)
        )
      }
    }
  }
}
