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

package controllers.agent

import audit.AuditingService
import audit.models.ChargeSummaryAudit
import config.featureswitch.{AgentViewer, FeatureSwitching, NewFinancialDetailsApi, Payment, TxmEventsApproved}
import config.{FrontendAppConfig, ItvcErrorHandler}
import controllers.agent.predicates.ClientConfirmedController
import controllers.agent.utils.SessionKeys
import implicits.{ImplicitDateFormatter, ImplicitDateFormatterImpl}
import models.financialDetails.{DocumentDetailWithDueDate, FinancialDetailsModel}
import play.api.Logger
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.Html
import services.{FinancialDetailsService, IncomeSourceDetailsService}
import uk.gov.hmrc.auth.core.AuthorisedFunctions
import uk.gov.hmrc.http.NotFoundException
import uk.gov.hmrc.play.language.LanguageUtils
import views.html.agent.ChargeSummary

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ChargeSummaryController @Inject()(chargeSummary: ChargeSummary,
                                        val authorisedFunctions: AuthorisedFunctions,
                                        financialDetailsService: FinancialDetailsService,
                                        incomeSourceDetailsService: IncomeSourceDetailsService,
                                        auditingService: AuditingService
                                       )(implicit val appConfig: FrontendAppConfig,
                                         val languageUtils: LanguageUtils,
                                         mcc: MessagesControllerComponents,
                                         dateFormatter: ImplicitDateFormatterImpl,
                                         implicit val ec: ExecutionContext,
                                         val itvcErrorHandler: ItvcErrorHandler)
  extends ClientConfirmedController with ImplicitDateFormatter with FeatureSwitching with I18nSupport {

  private def view(documentDetailWithDueDate: DocumentDetailWithDueDate, backLocation: Option[String], taxYear: Int)(implicit request: Request[_]): Html = {
    chargeSummary(
      documentDetailWithDueDate = documentDetailWithDueDate,
      implicitDateFormatter = dateFormatter,
      paymentEnabled = isEnabled(Payment),
      backUrl = backUrl(backLocation, taxYear)
    )
  }

  def showChargeSummary(taxYear: Int, chargeId: String): Action[AnyContent] = {
    Authenticated.async { implicit request =>
      implicit user =>
        if (isEnabled(AgentViewer)) {
          if (isEnabled(NewFinancialDetailsApi)) {
            financialDetailsService.getFinancialDetails(taxYear, getClientNino).map {
              case success: FinancialDetailsModel if success.documentDetails.exists(_.transactionId == chargeId) =>
                val backLocation = request.session.get(SessionKeys.chargeSummaryBackPage)

                val docDateDetail: DocumentDetailWithDueDate = success.findDocumentDetailByIdWithDueDate(chargeId).get
                getMtdItUserWithIncomeSources(incomeSourceDetailsService) map { mtdItUser =>
                  if (isEnabled(TxmEventsApproved)) {
                    auditingService.extendedAudit(ChargeSummaryAudit(
                      mtdItUser = mtdItUser,
                      docDateDetail = docDateDetail,
                      agentReferenceNumber = user.agentReferenceNumber
                    ))
                  }
                }
                Ok(view(success.findDocumentDetailByIdWithDueDate(chargeId).get, backLocation, taxYear))
              case _: FinancialDetailsModel =>
                Logger.warn(s"[ChargeSummaryController][showChargeSummary] Transaction id not found for tax year $taxYear")
                Redirect(controllers.agent.routes.HomeController.show())
              case _ =>
                Logger.warn("[ChargeSummaryController][showChargeSummary] Invalid response from financial transactions")
                itvcErrorHandler.showInternalServerError()
            }
          }
          else {
            Future.successful(Redirect(controllers.agent.routes.HomeController.show().url))
          }
        } else {
          Future.failed(new NotFoundException("[HomeController][home] - Agent viewer is disabled"))
        }
    }
  }

  def backUrl(backLocation: Option[String], taxYear: Int): String = backLocation match {
    case Some("taxYearOverview") => controllers.agent.routes.TaxYearOverviewController.show(taxYear).url + "#payments"
    case Some("paymentDue") => controllers.agent.nextPaymentDue.routes.PaymentDueController.show().url
    case _ => controllers.agent.routes.HomeController.show().url
  }

}
