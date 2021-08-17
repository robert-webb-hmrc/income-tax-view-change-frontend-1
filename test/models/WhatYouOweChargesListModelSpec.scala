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

package models

import assets.FinancialDetailsTestConstants._
import models.financialDetails.WhatYouOweChargesList
import models.outstandingCharges.OutstandingChargesModel
import org.scalatest.Matchers
import uk.gov.hmrc.play.test.UnitSpec

import java.time.LocalDate

class WhatYouOweChargesListModelSpec extends UnitSpec with Matchers {

  val outstandingCharges: OutstandingChargesModel = outstandingChargesModel(LocalDate.now().minusMonths(13).toString)

  def whatYouOweAllData(dunningLock: List[Option[String]] = noDunningLocks): WhatYouOweChargesList = WhatYouOweChargesList(
    dueInThirtyDaysList = financialDetailsDueIn30Days(dunningLock).getAllDocumentDetailsWithDueDates,
    futurePayments = financialDetailsDueInMoreThan30Days(dunningLock).getAllDocumentDetailsWithDueDates,
    overduePaymentList = financialDetailsOverdueData(dunningLock).getAllDocumentDetailsWithDueDates,
    outstandingChargesModel = Some(outstandingCharges)
  )

  def whatYouOweFinancialDataWithoutOutstandingCharges(dunningLock: List[Option[String]] = noDunningLocks): WhatYouOweChargesList = WhatYouOweChargesList(
    dueInThirtyDaysList = financialDetailsDueIn30Days(dunningLock).getAllDocumentDetailsWithDueDates,
    futurePayments = financialDetailsDueInMoreThan30Days(dunningLock).getAllDocumentDetailsWithDueDates,
    overduePaymentList = financialDetailsOverdueData(dunningLock).getAllDocumentDetailsWithDueDates
  )


  "The WhatYouOweChargesList model" when {

    "all values in model exists with tie breaker matching in OutstandingCharges Model" should {
      "bcdChargeTypeDefinedAndGreaterThanZero is true" in {
        whatYouOweAllData().bcdChargeTypeDefinedAndGreaterThanZero shouldBe true
      }
      "isChargesListEmpty is false" in {
        whatYouOweAllData().isChargesListEmpty shouldBe false
      }
      "getEarliestTaxYearAndAmountByDueDate should have correct values" in {
        whatYouOweAllData().getEarliestTaxYearAndAmountByDueDate._1 shouldBe LocalDate.now().minusMonths(13).getYear
        whatYouOweAllData().getEarliestTaxYearAndAmountByDueDate._2 shouldBe 123456.67
      }
      "hasDunningLock should return false if there are no dunningLocks" in {
        whatYouOweAllData().hasDunningLock shouldBe false
      }
      "hasDunningLock should return true if there are is one dunningLock" in {
        whatYouOweAllData(oneDunningLock).hasDunningLock shouldBe true
      }
      "hasDunningLock should return true if there are multiple dunningLocks" in {
        whatYouOweAllData(twoDunningLocks).hasDunningLock shouldBe true
      }
    }

    "all values in model exists except outstanding charges" should {
      "bcdChargeTypeDefinedAndGreaterThanZero is false" in {
        whatYouOweFinancialDataWithoutOutstandingCharges().bcdChargeTypeDefinedAndGreaterThanZero shouldBe false
      }
      "isChargesListEmpty is false" in {
        whatYouOweFinancialDataWithoutOutstandingCharges().isChargesListEmpty shouldBe false
      }
      "getEarliestTaxYearAndAmountByDueDate should have correct values" in {
        whatYouOweFinancialDataWithoutOutstandingCharges()
          .getEarliestTaxYearAndAmountByDueDate._1 shouldBe LocalDate.now().minusDays(10).getYear
        whatYouOweFinancialDataWithoutOutstandingCharges().getEarliestTaxYearAndAmountByDueDate._2 shouldBe 50.0
      }
    }
  }
}
