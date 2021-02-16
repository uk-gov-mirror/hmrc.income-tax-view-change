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

import assets.BaseIntegrationTestConstants._
import helpers.ComponentSpecBase
import helpers.servicemocks.DesPaymentAllocationsStub._
import models.paymentAllocations.{AllocationDetail, PaymentAllocations, PaymentDetails}
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSResponse

class PaymentAllocationsControllerISpec extends ComponentSpecBase {

  val paymentLot: String = "paymentLot"
  val paymentLotItem: String = "paymentLotItem"

  val from: String = "from"
  val to: String = "to"

  val paymentAllocations: PaymentAllocations = PaymentAllocations(
    amount = Some(1000.00),
    method = Some("method"),
    transactionDate = Some("transactionDate"),
    reference = Some("reference"),
    allocations = Seq(
      AllocationDetail(
        transactionId = Some("transactionId"),
        from = Some("from"),
        to = Some("to"),
        `type` = Some("type"),
        amount = Some(1500.00),
        clearedAmount = Some(500.00)
      )
    )
  )

  val paymentDetails: PaymentDetails = PaymentDetails(
    Seq(paymentAllocations)
  )

  val paymentAllocationsJson: JsObject = Json.obj(
    "paymentDetails" -> Json.arr(
      Json.obj(
        "paymentAmount" -> 1000.00,
        "paymentMethod" -> "method",
        "valueDate" -> "transactionDate",
        "paymentReference" -> "reference",
        "sapClearingDocsDetails" -> Json.arr(
          Json.obj(
            "sapDocNumber" -> "transactionId",
            "taxPeriodStartDate" -> "from",
            "taxPeriodEndDate" -> "to",
            "chargeType" -> "type",
            "amount" -> 1500.00,
            "clearedAmount" -> 500.00
          )
        )
      )
    )
  )

  s"GET ${controllers.routes.PaymentAllocationsController.getPaymentAllocations(testNino, paymentLot, paymentLotItem)}" should {
    s"return $OK" when {
      "payment allocations are successfully retrieved" in {
        Given("the user is authorised")
        isAuthorised(true)

        And("the call to retrieve payment allocations is stubbed")
        stubGetPaymentAllocations(testNino, paymentLot, paymentLotItem)(
          status = OK,
          response = paymentAllocationsJson
        )

        When(s"I call GET ${controllers.routes.PaymentAllocationsController.getPaymentAllocations(testNino, paymentLot, paymentLotItem)}")
        val res: WSResponse = IncomeTaxViewChange.getPaymentAllocations(testNino, paymentLot, paymentLotItem)

        Then("a successful response is returned with the payment allocations")
        res should have(
          httpStatus(OK),
          jsonBodyMatching(Json.toJson(paymentAllocations))
        )
      }
    }
    s"return $INTERNAL_SERVER_ERROR" when {
      "an unexpected status was returned when retrieving payment allocations" in {
        Given("the user is authorised")
        isAuthorised(true)

        And("the call to retrieve payment allocations is stubbed")
        stubGetPaymentAllocations(testNino, paymentLot, paymentLotItem)(
          status = BAD_REQUEST
        )

        When(s"I call GET ${controllers.routes.PaymentAllocationsController.getPaymentAllocations(testNino, paymentLot, paymentLotItem)}")
        val res: WSResponse = IncomeTaxViewChange.getPaymentAllocations(testNino, paymentLot, paymentLotItem)

        Then("an internal server error response is returned")
        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }
    }
  }

  s"GET ${controllers.routes.PaymentAllocationsController.getPreviousPayments(testNino, from, to)}" should {
    s"return $OK" when {
      "previous payments are successfully retrieved" in {
        Given("the user is authorised")
        isAuthorised(true)

        And("the call to retrieve payment allocations is stubbed")
        stubGetPreviousPayments(testNino, from, to)(
          status = OK,
          response = paymentAllocationsJson
        )

        When(s"I call GET ${controllers.routes.PaymentAllocationsController.getPreviousPayments(testNino, from, to)}")
        val res: WSResponse = IncomeTaxViewChange.getPreviousPayments(testNino, from, to)

        Then("a successful response is returned with the previous payments")
        res should have(
          httpStatus(OK),
          jsonBodyMatching(Json.toJson(paymentDetails))
        )
      }
    }
  }
}
