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
package assets

import assets.BaseIntegrationTestConstants.testCalcId
import enums.Estimate
import models.calculation._
import play.api.libs.json.{JsValue, Json}

object CalcDataIntegrationTestConstants {

  val calculationDataSuccessWithEoYModel = CalculationDataModel(
    totalIncomeTaxNicYtd = 90500.00,
    totalTaxableIncome = 198500.00,
    personalAllowance = 11500.00,
    taxReliefs = 1000,
    totalIncomeAllowancesUsed = 12005.00,
    incomeReceived = IncomeReceivedModel(
      selfEmployment = 200000.00,
      ukProperty = 10000.00,
      bankBuildingSocietyInterest = 2000.00,
      ukDividends = 11000.00
    ),
    savingsAndGains = SavingsAndGainsModel(
      startBand = BandModel(
        taxableIncome = 1.00,
        taxRate = 0.0,
        taxAmount = 0.0
      ),
      zeroBand = BandModel(
        taxableIncome = 20.00,
        taxRate = 0.0,
        taxAmount = 0.0
      ),
      basicBand = BandModel(
        taxableIncome = 500.0,
        taxRate = 20.0,
        taxAmount = 100.0
      ),
      higherBand = BandModel(
        taxableIncome = 1000.0,
        taxRate = 40.0,
        taxAmount = 400.0
      ),
      additionalBand = BandModel(
        taxableIncome = 479.0,
        taxRate = 45.0,
        taxAmount = 215.55
      )
    ),
    dividends = DividendsModel(
      totalAmount = 5000.0,
      Seq(
        DividendsBandModel(
          name = "basic-band",
          rate = 7.5, threshold = None,
          apportionedThreshold = None,
          income = 1000.0,
          amount = 75.0
        ),
        DividendsBandModel(
          name = "higher-band",
          rate = 37.5, threshold = None,
          apportionedThreshold = None,
          income = 2000.0,
          amount = 750.0
        ),
        DividendsBandModel(
          name = "additional-band",
          rate = 38.1,
          threshold = None,
          apportionedThreshold = None,
          income = 3000.0,
          amount = 1143.0
        )
      )
    ),
    nic = NicModel(
      class2 = 10000.00,
      class4 = 14000.00
    ),
    giftAid = GiftAidModel(
      paymentsMade = 150,
      rate = 0.0,
      taxableAmount = 0
    ),
    eoyEstimate = Some(EoyEstimate(25000.00)),
    payAndPensionsProfitBands = List(
      TaxBandModel("BRT", 20.0, 20000.00, 4000.00),
      TaxBandModel("HRT", 40.0, 100000.00, 40000.00),
      TaxBandModel("ART", 45.0, 50000.00, 22500.00)
    )
  )

  val calculationDataSuccessModel = CalculationDataModel(
    totalIncomeTaxNicYtd = 90500.00,
    totalTaxableIncome = 198500.00,
    personalAllowance = 11500.00,
    taxReliefs = 1000,
    totalIncomeAllowancesUsed = 12005.00,
    incomeReceived = IncomeReceivedModel(
      selfEmployment = 200000.00,
      ukProperty = 10000.00,
      bankBuildingSocietyInterest = 2000.00,
      ukDividends = 11000.00
    ),
    savingsAndGains = SavingsAndGainsModel(
      startBand = BandModel(
        taxableIncome = 1.00,
        taxRate = 0.0,
        taxAmount = 0.0
      ),
      zeroBand = BandModel(
        taxableIncome = 20.00,
        taxRate = 0.0,
        taxAmount = 0.0
      ),
      basicBand = BandModel(
        taxableIncome = 500.0,
        taxRate = 20.0,
        taxAmount = 100.0
      ),
      higherBand = BandModel(
        taxableIncome = 1000.0,
        taxRate = 40.0,
        taxAmount = 400.0
      ),
      additionalBand = BandModel(
        taxableIncome = 479.0,
        taxRate = 45.0,
        taxAmount = 215.55
      )
    ),
    dividends = DividendsModel(
      totalAmount = 5000.0,
      Seq(
        DividendsBandModel(
          name = "basic-band",
          rate = 7.5, threshold = None,
          apportionedThreshold = None,
          income = 1000.0,
          amount = 75.0
        ),
        DividendsBandModel(
          name = "higher-band",
          rate = 37.5, threshold = None,
          apportionedThreshold = None,
          income = 2000.0,
          amount = 750.0
        ),
        DividendsBandModel(
          name = "additional-band",
          rate = 38.1,
          threshold = None,
          apportionedThreshold = None,
          income = 3000.0,
          amount = 1143.0
        )
      )
    ),
    nic = NicModel(
      class2 = 10000.00,
      class4 = 14000.00
    ),
    giftAid = GiftAidModel(
      paymentsMade = 150,
      rate = 0.0,
      taxableAmount = 0
    ),
    payAndPensionsProfitBands = List(
      TaxBandModel("BRT", 20.0, 20000.00, 4000.00),
      TaxBandModel("HRT", 40.0, 100000.00, 40000.00),
      TaxBandModel("ART", 45.0, 50000.00, 22500.00)
    )
  )

  val calculationDataErrorModel = CalculationDataErrorModel(code = 500, message = "Calculation Error Model Response")

  val calculationDataSuccessWithEoyJson: JsValue =
    Json.obj(
      "nationalRegime" -> "UK",
      "incomeTaxYTD" -> 90500,
      "incomeTaxThisPeriod" -> 2000,
      "payFromAllEmployments" -> 0,
      "benefitsAndExpensesReceived" -> 0,
      "totalIncomeAllowancesUsed" -> 12005,
      "allowableExpenses" -> 0,
      "payFromAllEmploymentsAfterExpenses" -> 0,
      "shareSchemes" -> 0,
      "profitFromSelfEmployment" -> 200000,
      "profitFromPartnerships" -> 0,
      "profitFromUkLandAndProperty" -> 10000,
      "dividendsFromForeignCompanies" -> 0,
      "foreignIncome" -> 0,
      "trustsAndEstates" -> 0,
      "interestReceivedFromUkBanksAndBuildingSocieties" -> 2000,
      "dividendsFromUkCompanies" -> 11000,
      "ukPensionsAndStateBenefits" -> 0,
      "gainsOnLifeInsurance" -> 0,
      "otherIncome" -> 0,
      "totalIncomeReceived" -> 230000,
      "paymentsIntoARetirementAnnuity" -> 0,
      "foreignTaxOnEstates" -> 0,
      "incomeTaxRelief" -> 0,
      "incomeTaxReliefReducedToMaximumAllowable" -> 0,
      "annuities" -> 0,
      "giftOfInvestmentsAndPropertyToCharity" -> 0,
      "personalAllowance" -> 11500,
      "marriageAllowanceTransfer" -> 0,
      "blindPersonAllowance" -> 0,
      "blindPersonSurplusAllowanceFromSpouse" -> 0,
      "incomeExcluded" -> 0,
      "totalIncomeOnWhichTaxIsDue" -> 198500,
      "payPensionsExtender" -> 0,
      "giftExtender" -> 0,
      "extendedBR" -> 0,
      "payPensionsProfitAtBRT" -> 20000,
      "incomeTaxOnPayPensionsProfitAtBRT" -> 4000,
      "payPensionsProfitAtHRT" -> 100000,
      "incomeTaxOnPayPensionsProfitAtHRT" -> 40000,
      "payPensionsProfitAtART" -> 50000,
      "incomeTaxOnPayPensionsProfitAtART" -> 22500,
      "netPropertyFinanceCosts" -> 0,
      "interestReceivedAtStartingRate" -> 1,
      "incomeTaxOnInterestReceivedAtStartingRate" -> 0,
      "interestReceivedAtZeroRate" -> 20,
      "incomeTaxOnInterestReceivedAtZeroRate" -> 0,
      "interestReceivedAtBRT" -> 500,
      "incomeTaxOnInterestReceivedAtBRT" -> 100,
      "interestReceivedAtHRT" -> 1000,
      "incomeTaxOnInterestReceivedAtHRT" -> 400,
      "interestReceivedAtART" -> 479,
      "incomeTaxOnInterestReceivedAtART" -> 215.55,
      "dividendsAtZeroRate" -> 0,
      "incomeTaxOnDividendsAtZeroRate" -> 0,
      "dividendsAtBRT" -> 1000,
      "incomeTaxOnDividendsAtBRT" -> 75,
      "dividendsAtHRT" -> 2000,
      "incomeTaxOnDividendsAtHRT" -> 750,
      "dividendsAtART" -> 3000,
      "incomeTaxOnDividendsAtART" -> 1143,
      "totalIncomeOnWhichTaxHasBeenCharged" -> 0,
      "taxOnOtherIncome" -> 0,
      "incomeTaxDue" -> 66500,
      "incomeTaxCharged" -> 0,
      "deficiencyRelief" -> 0,
      "topSlicingRelief" -> 0,
      "ventureCapitalTrustRelief" -> 0,
      "enterpriseInvestmentSchemeRelief" -> 0,
      "seedEnterpriseInvestmentSchemeRelief" -> 0,
      "communityInvestmentTaxRelief" -> 0,
      "socialInvestmentTaxRelief" -> 0,
      "maintenanceAndAlimonyPaid" -> 0,
      "marriedCouplesAllowance" -> 0,
      "marriedCouplesAllowanceRelief" -> 0,
      "surplusMarriedCouplesAllowance" -> 0,
      "surplusMarriedCouplesAllowanceRelief" -> 0,
      "notionalTaxFromLifePolicies" -> 0,
      "notionalTaxFromDividendsAndOtherIncome" -> 0,
      "foreignTaxCreditRelief" -> 0,
      "incomeTaxDueAfterAllowancesAndReliefs" -> 0,
      "giftAidPaymentsAmount" -> 0,
      "giftAidTaxDue" -> 0,
      "capitalGainsTaxDue" -> 0,
      "remittanceForNonDomiciles" -> 0,
      "highIncomeChildBenefitCharge" -> 0,
      "totalGiftAidTaxReduced" -> 0,
      "incomeTaxDueAfterGiftAidReduction" -> 0,
      "annuityAmount" -> 0,
      "taxDueOnAnnuity" -> 0,
      "taxCreditsOnDividendsFromUkCompanies" -> 0,
      "incomeTaxDueAfterDividendTaxCredits" -> 0,
      "nationalInsuranceContributionAmount" -> 0,
      "nationalInsuranceContributionCharge" -> 0,
      "nationalInsuranceContributionSupAmount" -> 0,
      "nationalInsuranceContributionSupCharge" -> 0,
      "totalClass4Charge" -> 14000,
      "nationalInsuranceClass1Amount" -> 0,
      "nationalInsuranceClass2Amount" -> 10000,
      "nicTotal" -> 24000,
      "underpaidTaxForPreviousYears" -> 0,
      "studentLoanRepayments" -> 0,
      "pensionChargesGross" -> 0,
      "pensionChargesTaxPaid" -> 0,
      "totalPensionSavingCharges" -> 0,
      "pensionLumpSumAmount" -> 0,
      "pensionLumpSumRate" -> 0,
      "statePensionLumpSumAmount" -> 0,
      "remittanceBasisChargeForNonDomiciles" -> 0,
      "additionalTaxDueOnPensions" -> 0,
      "additionalTaxReliefDueOnPensions" -> 0,
      "incomeTaxDueAfterPensionDeductions" -> 0,
      "employmentsPensionsAndBenefits" -> 0,
      "outstandingDebtCollectedThroughPaye" -> 0,
      "payeTaxBalance" -> 0,
      "cisAndTradingIncome" -> 0,
      "partnerships" -> 0,
      "ukLandAndPropertyTaxPaid" -> 0,
      "foreignIncomeTaxPaid" -> 0,
      "trustAndEstatesTaxPaid" -> 0,
      "overseasIncomeTaxPaid" -> 0,
      "interestReceivedTaxPaid" -> 0,
      "voidISAs" -> 0,
      "otherIncomeTaxPaid" -> 0,
      "underpaidTaxForPriorYear" -> 0,
      "totalTaxDeducted" -> 0,
      "incomeTaxOverpaid" -> 0,
      "incomeTaxDueAfterDeductions" -> 0,
      "propertyFinanceTaxDeduction" -> 0,
      "taxableCapitalGains" -> 0,
      "capitalGainAtEntrepreneurRate" -> 0,
      "incomeTaxOnCapitalGainAtEntrepreneurRate" -> 0,
      "capitalGrainsAtLowerRate" -> 0,
      "incomeTaxOnCapitalGainAtLowerRate" -> 0,
      "capitalGainAtHigherRate" -> 0,
      "incomeTaxOnCapitalGainAtHigherTax" -> 0,
      "capitalGainsTaxAdjustment" -> 0,
      "foreignTaxCreditReliefOnCapitalGains" -> 0,
      "liabilityFromOffShoreTrusts" -> 0,
      "taxOnGainsAlreadyCharged" -> 0,
      "totalCapitalGainsTax" -> 0,
      "incomeAndCapitalGainsTaxDue" -> 0,
      "taxRefundedInYear" -> 0,
      "unpaidTaxCalculatedForEarlierYears" -> 0,
      "marriageAllowanceTransferAmount" -> 0,
      "marriageAllowanceTransferRelief" -> 0,
      "marriageAllowanceTransferMaximumAllowable" -> 0,
      "nationalRegime" -> "0",
      "allowance" -> 0,
      "limitBRT" -> 0,
      "limitHRT" -> 0,
      "rateBRT" -> 20,
      "rateHRT" -> 40,
      "rateART" -> 45,
      "limitAIA" -> 0,
      "limitAIA" -> 0,
      "allowanceBRT" -> 0,
      "interestAllowanceHRT" -> 0,
      "interestAllowanceBRT" -> 0,
      "dividendAllowance" -> 5000,
      "dividendBRT" -> 7.5,
      "dividendHRT" -> 37.5,
      "dividendART" -> 38.1,
      "class2NICsLimit" -> 0,
      "class2NICsPerWeek" -> 0,
      "class4NICsLimitBR" -> 0,
      "class4NICsLimitHR" -> 0,
      "class4NICsBRT" -> 0,
      "class4NICsHRT" -> 0,
      "proportionAllowance" -> 11500,
      "proportionLimitBRT" -> 0,
      "proportionLimitHRT" -> 0,
      "proportionalTaxDue" -> 0,
      "proportionInterestAllowanceBRT" -> 0,
      "proportionInterestAllowanceHRT" -> 0,
      "proportionDividendAllowance" -> 0,
      "proportionPayPensionsProfitAtART" -> 0,
      "proportionIncomeTaxOnPayPensionsProfitAtART" -> 0,
      "proportionPayPensionsProfitAtBRT" -> 0,
      "proportionIncomeTaxOnPayPensionsProfitAtBRT" -> 0,
      "proportionPayPensionsProfitAtHRT" -> 0,
      "proportionIncomeTaxOnPayPensionsProfitAtHRT" -> 0,
      "proportionInterestReceivedAtZeroRate" -> 0,
      "proportionIncomeTaxOnInterestReceivedAtZeroRate" -> 0,
      "proportionInterestReceivedAtBRT" -> 0,
      "proportionIncomeTaxOnInterestReceivedAtBRT" -> 0,
      "proportionInterestReceivedAtHRT" -> 0,
      "proportionIncomeTaxOnInterestReceivedAtHRT" -> 0,
      "proportionInterestReceivedAtART" -> 0,
      "proportionIncomeTaxOnInterestReceivedAtART" -> 0,
      "proportionDividendsAtZeroRate" -> 0,
      "proportionIncomeTaxOnDividendsAtZeroRate" -> 0,
      "proportionDividendsAtBRT" -> 0,
      "proportionIncomeTaxOnDividendsAtBRT" -> 0,
      "proportionDividendsAtHRT" -> 0,
      "proportionIncomeTaxOnDividendsAtHRT" -> 0,
      "proportionDividendsAtART" -> 0,
      "proportionIncomeTaxOnDividendsAtART" -> 0,
      "proportionClass2NICsLimit" -> 0,
      "proportionClass4NICsLimitBR" -> 0,
      "proportionClass4NICsLimitHR" -> 0,
      "proportionReducedAllowanceLimit" -> 0,
      "eoyEstimate" -> Json.obj(
        "selfEmployment" -> Json.arr(
          Json.obj(
            "id" -> "selfEmploymentId1",
            "taxableIncome" -> 89999999.99,
            "supplied" -> true,
            "finalised" -> true
          ),
          Json.obj(
            "id" -> "selfEmploymentId2",
            "taxableIncome" -> 89999999.99,
            "supplied" -> true,
            "finalised" -> true
          )
        ),
        "ukProperty" -> Json.arr(
          Json.obj(
            "taxableIncome" -> 89999999.99,
            "supplied" -> true,
            "finalised" -> true
          )
        ),
        "totalTaxableIncome" -> 89999999.99,
        "incomeTaxAmount" -> 89999999.99,
        "nic2" -> 89999999.99,
        "nic4" -> 89999999.99,
        "totalNicAmount" -> 9999999.99,
        "incomeTaxNicAmount" -> 66000.00
      ),
      "incomeTax" -> Json.obj(
        "payAndPensionsProfit" -> Json.obj(
          "band" -> Json.arr(Json.obj(
            "name" -> "BRT",
            "rate" -> 20.0,
            "income" -> 20000.00,
            "amount" -> 4000.00
          ), Json.obj(
            "name" -> "HRT",
            "rate" -> 40.0,
            "income" -> 100000.00,
            "amount" -> 40000.00
          ), Json.obj(
            "name" -> "ART",
            "rate" -> 45.0,
            "income" -> 50000.00,
            "amount" -> 22500.00
          )
          )
        ),
        "dividends" -> Json.obj(
          "totalAmount" -> 5000,
          "band" -> Json.arr(
            Json.obj(
              "name" -> "basic-band",
              "rate" -> 7.5,
              "income" -> 1000,
              "amount" -> 75.0
            ),
            Json.obj(
              "name" -> "higher-band",
              "rate" -> 37.5,
              "income" -> 2000,
              "amount" -> 750
            ),
            Json.obj(
              "name" -> "additional-band",
              "rate" -> 38.1,
              "income" -> 3000,
              "amount" -> 1143
            )))
      )
    )

  val calculationDataSuccessJson: JsValue = Json.obj(
    "nationalRegime" -> "UK",
    "incomeTaxYTD" -> 90500,
    "incomeTaxThisPeriod" -> 2000,
    "payFromAllEmployments" -> 0,
    "totalIncomeAllowancesUsed" -> 12005,
    "benefitsAndExpensesReceived" -> 0,
    "allowableExpenses" -> 0,
    "payFromAllEmploymentsAfterExpenses" -> 0,
    "shareSchemes" -> 0,
    "profitFromSelfEmployment" -> 200000,
    "profitFromPartnerships" -> 0,
    "profitFromUkLandAndProperty" -> 10000,
    "dividendsFromForeignCompanies" -> 0,
    "foreignIncome" -> 0,
    "trustsAndEstates" -> 0,
    "interestReceivedFromUkBanksAndBuildingSocieties" -> 2000,
    "dividendsFromUkCompanies" -> 11000,
    "ukPensionsAndStateBenefits" -> 0,
    "gainsOnLifeInsurance" -> 0,
    "otherIncome" -> 0,
    "totalIncomeReceived" -> 230000,
    "paymentsIntoARetirementAnnuity" -> 0,
    "foreignTaxOnEstates" -> 0,
    "incomeTaxRelief" -> 0,
    "incomeTaxReliefReducedToMaximumAllowable" -> 0,
    "annuities" -> 0,
    "giftOfInvestmentsAndPropertyToCharity" -> 0,
    "personalAllowance" -> 11500,
    "marriageAllowanceTransfer" -> 0,
    "blindPersonAllowance" -> 0,
    "blindPersonSurplusAllowanceFromSpouse" -> 0,
    "incomeExcluded" -> 0,
    "totalIncomeOnWhichTaxIsDue" -> 198500,
    "payPensionsExtender" -> 0,
    "giftExtender" -> 0,
    "extendedBR" -> 0,
    "payPensionsProfitAtBRT" -> 20000,
    "incomeTaxOnPayPensionsProfitAtBRT" -> 4000,
    "payPensionsProfitAtHRT" -> 100000,
    "incomeTaxOnPayPensionsProfitAtHRT" -> 40000,
    "payPensionsProfitAtART" -> 50000,
    "incomeTaxOnPayPensionsProfitAtART" -> 22500,
    "netPropertyFinanceCosts" -> 0,
    "interestReceivedAtStartingRate" -> 1,
    "incomeTaxOnInterestReceivedAtStartingRate" -> 0,
    "interestReceivedAtZeroRate" -> 20,
    "incomeTaxOnInterestReceivedAtZeroRate" -> 0,
    "interestReceivedAtBRT" -> 500,
    "incomeTaxOnInterestReceivedAtBRT" -> 100,
    "interestReceivedAtHRT" -> 1000,
    "incomeTaxOnInterestReceivedAtHRT" -> 400,
    "interestReceivedAtART" -> 479,
    "incomeTaxOnInterestReceivedAtART" -> 215.55,
    "dividendsAtZeroRate" -> 0,
    "incomeTaxOnDividendsAtZeroRate" -> 0,
    "dividendsAtBRT" -> 1000,
    "incomeTaxOnDividendsAtBRT" -> 75,
    "dividendsAtHRT" -> 2000,
    "incomeTaxOnDividendsAtHRT" -> 750,
    "dividendsAtART" -> 3000,
    "incomeTaxOnDividendsAtART" -> 1143,
    "totalIncomeOnWhichTaxHasBeenCharged" -> 0,
    "taxOnOtherIncome" -> 0,
    "incomeTaxDue" -> 66500,
    "incomeTaxCharged" -> 0,
    "deficiencyRelief" -> 0,
    "topSlicingRelief" -> 0,
    "ventureCapitalTrustRelief" -> 0,
    "enterpriseInvestmentSchemeRelief" -> 0,
    "seedEnterpriseInvestmentSchemeRelief" -> 0,
    "communityInvestmentTaxRelief" -> 0,
    "socialInvestmentTaxRelief" -> 0,
    "maintenanceAndAlimonyPaid" -> 0,
    "marriedCouplesAllowance" -> 0,
    "marriedCouplesAllowanceRelief" -> 0,
    "surplusMarriedCouplesAllowance" -> 0,
    "surplusMarriedCouplesAllowanceRelief" -> 0,
    "notionalTaxFromLifePolicies" -> 0,
    "notionalTaxFromDividendsAndOtherIncome" -> 0,
    "foreignTaxCreditRelief" -> 0,
    "incomeTaxDueAfterAllowancesAndReliefs" -> 0,
    "giftAidPaymentsAmount" -> 0,
    "giftAidTaxDue" -> 0,
    "capitalGainsTaxDue" -> 0,
    "remittanceForNonDomiciles" -> 0,
    "highIncomeChildBenefitCharge" -> 0,
    "totalGiftAidTaxReduced" -> 0,
    "incomeTaxDueAfterGiftAidReduction" -> 0,
    "annuityAmount" -> 0,
    "taxDueOnAnnuity" -> 0,
    "taxCreditsOnDividendsFromUkCompanies" -> 0,
    "incomeTaxDueAfterDividendTaxCredits" -> 0,
    "nationalInsuranceContributionAmount" -> 0,
    "nationalInsuranceContributionCharge" -> 0,
    "nationalInsuranceContributionSupAmount" -> 0,
    "nationalInsuranceContributionSupCharge" -> 0,
    "totalClass4Charge" -> 14000,
    "nationalInsuranceClass1Amount" -> 0,
    "nationalInsuranceClass2Amount" -> 10000,
    "nicTotal" -> 24000,
    "underpaidTaxForPreviousYears" -> 0,
    "studentLoanRepayments" -> 0,
    "pensionChargesGross" -> 0,
    "pensionChargesTaxPaid" -> 0,
    "totalPensionSavingCharges" -> 0,
    "pensionLumpSumAmount" -> 0,
    "pensionLumpSumRate" -> 0,
    "statePensionLumpSumAmount" -> 0,
    "remittanceBasisChargeForNonDomiciles" -> 0,
    "additionalTaxDueOnPensions" -> 0,
    "additionalTaxReliefDueOnPensions" -> 0,
    "incomeTaxDueAfterPensionDeductions" -> 0,
    "employmentsPensionsAndBenefits" -> 0,
    "outstandingDebtCollectedThroughPaye" -> 0,
    "payeTaxBalance" -> 0,
    "cisAndTradingIncome" -> 0,
    "partnerships" -> 0,
    "ukLandAndPropertyTaxPaid" -> 0,
    "foreignIncomeTaxPaid" -> 0,
    "trustAndEstatesTaxPaid" -> 0,
    "overseasIncomeTaxPaid" -> 0,
    "interestReceivedTaxPaid" -> 0,
    "voidISAs" -> 0,
    "otherIncomeTaxPaid" -> 0,
    "underpaidTaxForPriorYear" -> 0,
    "totalTaxDeducted" -> 0,
    "incomeTaxOverpaid" -> 0,
    "incomeTaxDueAfterDeductions" -> 0,
    "propertyFinanceTaxDeduction" -> 0,
    "taxableCapitalGains" -> 0,
    "capitalGainAtEntrepreneurRate" -> 0,
    "incomeTaxOnCapitalGainAtEntrepreneurRate" -> 0,
    "capitalGrainsAtLowerRate" -> 0,
    "incomeTaxOnCapitalGainAtLowerRate" -> 0,
    "capitalGainAtHigherRate" -> 0,
    "incomeTaxOnCapitalGainAtHigherTax" -> 0,
    "capitalGainsTaxAdjustment" -> 0,
    "foreignTaxCreditReliefOnCapitalGains" -> 0,
    "liabilityFromOffShoreTrusts" -> 0,
    "taxOnGainsAlreadyCharged" -> 0,
    "totalCapitalGainsTax" -> 0,
    "incomeAndCapitalGainsTaxDue" -> 0,
    "taxRefundedInYear" -> 0,
    "unpaidTaxCalculatedForEarlierYears" -> 0,
    "marriageAllowanceTransferAmount" -> 0,
    "marriageAllowanceTransferRelief" -> 0,
    "marriageAllowanceTransferMaximumAllowable" -> 0,
    "nationalRegime" -> "0",
    "allowance" -> 0,
    "limitBRT" -> 0,
    "limitHRT" -> 0,
    "rateBRT" -> 20,
    "rateHRT" -> 40,
    "rateART" -> 45,
    "limitAIA" -> 0,
    "limitAIA" -> 0,
    "allowanceBRT" -> 0,
    "interestAllowanceHRT" -> 0,
    "interestAllowanceBRT" -> 0,
    "dividendAllowance" -> 5000,
    "dividendBRT" -> 7.5,
    "dividendHRT" -> 37.5,
    "dividendART" -> 38.1,
    "class2NICsLimit" -> 0,
    "class2NICsPerWeek" -> 0,
    "class4NICsLimitBR" -> 0,
    "class4NICsLimitHR" -> 0,
    "class4NICsBRT" -> 0,
    "class4NICsHRT" -> 0,
    "proportionAllowance" -> 11500,
    "proportionLimitBRT" -> 0,
    "proportionLimitHRT" -> 0,
    "proportionalTaxDue" -> 0,
    "proportionInterestAllowanceBRT" -> 0,
    "proportionInterestAllowanceHRT" -> 0,
    "proportionDividendAllowance" -> 0,
    "proportionPayPensionsProfitAtART" -> 0,
    "proportionIncomeTaxOnPayPensionsProfitAtART" -> 0,
    "proportionPayPensionsProfitAtBRT" -> 0,
    "proportionIncomeTaxOnPayPensionsProfitAtBRT" -> 0,
    "proportionPayPensionsProfitAtHRT" -> 0,
    "proportionIncomeTaxOnPayPensionsProfitAtHRT" -> 0,
    "proportionInterestReceivedAtZeroRate" -> 0,
    "proportionIncomeTaxOnInterestReceivedAtZeroRate" -> 0,
    "proportionInterestReceivedAtBRT" -> 0,
    "proportionIncomeTaxOnInterestReceivedAtBRT" -> 0,
    "proportionInterestReceivedAtHRT" -> 0,
    "proportionIncomeTaxOnInterestReceivedAtHRT" -> 0,
    "proportionInterestReceivedAtART" -> 0,
    "proportionIncomeTaxOnInterestReceivedAtART" -> 0,
    "proportionDividendsAtZeroRate" -> 0,
    "proportionIncomeTaxOnDividendsAtZeroRate" -> 0,
    "proportionDividendsAtBRT" -> 0,
    "proportionIncomeTaxOnDividendsAtBRT" -> 0,
    "proportionDividendsAtHRT" -> 0,
    "proportionIncomeTaxOnDividendsAtHRT" -> 0,
    "proportionDividendsAtART" -> 0,
    "proportionIncomeTaxOnDividendsAtART" -> 0,
    "proportionClass2NICsLimit" -> 0,
    "proportionClass4NICsLimitBR" -> 0,
    "proportionClass4NICsLimitHR" -> 0,
    "proportionReducedAllowanceLimit" -> 0,
    "incomeTax" -> Json.obj(
      "payAndPensionsProfit" -> Json.obj(
        "band" -> Json.arr(Json.obj(
          "name" -> "BRT",
          "rate" -> 20.0,
          "income" -> 20000.00,
          "amount" -> 4000.00
        ), Json.obj(
          "name" -> "HRT",
          "rate" -> 40.0,
          "income" -> 100000.00,
          "amount" -> 40000.00
        ), Json.obj(
          "name" -> "ART",
          "rate" -> 45.0,
          "income" -> 50000.00,
          "amount" -> 22500.00
        )
        )
      ),
      "dividends" -> Json.obj(
        "totalAmount" -> 5000,
        "band" -> Json.arr(
          Json.obj(
            "name" -> "basic-band",
            "rate" -> 7.5,
            "income" -> 1000,
            "amount" -> 75.0
          ),
          Json.obj(
            "name" -> "higher-band",
            "rate" -> 37.5,
            "income" -> 2000,
            "amount" -> 750
          ),
          Json.obj(
            "name" -> "additional-band",
            "rate" -> 38.1,
            "income" -> 3000,
            "amount" -> 1143
          )))
    )
  )

  val taxCalculationResponse: CalculationModel = CalculationModel(testCalcId,
    Some(90500.00),
    Some("2017-07-06T12:34:56.789Z"),
    None,
    None,
    None
  )

  val taxCalculationCrystallisedResponse: CalculationModel = CalculationModel(testCalcId,
    Some(90500.00),
    Some("2017-07-06T12:34:56.789Z"),
    Some(true),
    None,
    None
  )

  val latestCalcModel: CalculationModel =
    CalculationModel(
      "CALCID",
      Some(543.21),
      Some("2017-07-06T12:34:56.789Z"),
      None,
      Some(123.45),
      Some(987.65)
    )
}