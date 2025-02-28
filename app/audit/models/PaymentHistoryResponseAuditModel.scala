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

package audit.models

import audit.Utilities.userAuditDetails
import auth.MtdItUser
import config.featureswitch.R7bTxmEvents
import models.financialDetails.Payment
import play.api.libs.json.{JsObject, JsValue, Json}
import utils.Utilities.JsonUtil

case class PaymentHistoryResponseAuditModel(mtdItUser: MtdItUser[_],
                                            payments: Seq[Payment],
                                            CutOverCreditsEnabled: Boolean,
                                            R7bTxmEvents: Boolean) extends ExtendedAuditModel {

  override val transactionName: String = "payment-history-response"
  override val auditType: String = "PaymentHistoryResponse"

  private def paymentHistoryDetail(payment: Payment): JsObject =
    paymentHistoryDescriptionType(payment) ++
      ("paymentDate", payment.date) ++
      ("amount", payment.amount)


  private def paymentHistoryDescriptionType(payment: Payment): JsObject = {
    if (R7bTxmEvents) {
      if (payment.credit.isDefined && CutOverCreditsEnabled) {
        Json.obj("description" -> "Payment from an earlier tax year")
      }
      else {
        Json.obj("description" -> "Payment Made to HMRC")
      }
    }
    else Json.obj("description" -> "Payment Made to HMRC")
  }


  private val paymentHistory: Seq[JsObject] = payments.map(paymentHistoryDetail)

  override val detail: JsValue = userAuditDetails(mtdItUser) ++
    Json.obj("paymentHistory" -> paymentHistory)

}
