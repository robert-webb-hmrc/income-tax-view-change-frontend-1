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

package models.paymentAllocations

import play.api.libs.json.{Format, Json}

sealed trait PaymentAllocationsResponse

case class PaymentAllocations(amount: Option[BigDecimal],
                              method: Option[String],
                              transactionDate: Option[String],
                              reference: Option[String],
                              allocations: Seq[AllocationDetail]) extends PaymentAllocationsResponse


object PaymentAllocations {
  implicit val format: Format[PaymentAllocations] = Json.format[PaymentAllocations]
}

case class PaymentAllocationsError(code: Int, message: String) extends PaymentAllocationsResponse
