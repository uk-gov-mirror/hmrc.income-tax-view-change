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
import helpers.servicemocks.DesChargesStub._
import models.financialDetails.responses.ChargesResponse
import models.financialDetails.{Charge, SubItem}
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.WSResponse

class FinancialDetailChargesControllerISpec extends ComponentSpecBase {

  val from: String = "from"
  val to: String = "to"

  val charges1: Charge = Charge(
    taxYear = "2018",
    transactionId = "transactionId",
    transactionDate = Some("transactionDate"),
    `type` = Some("type"),
    totalAmount = Some(BigDecimal("1000.00")),
    originalAmount=Some(BigDecimal("500.00")),
    outstandingAmount = Some(BigDecimal("500.00")),
    clearedAmount = Some(BigDecimal("500.00")),
		chargeType = Some("POA1"),
		mainType = Some("4920"),
    items = Some(Seq(
      SubItem(
        subItemId = Some("1"),
        amount = Some(BigDecimal("100.00")),
        clearingDate = Some("clearingDate"),
        clearingReason = Some("clearingReason"),
        outgoingPaymentMethod = Some("outgoingPaymentMethod"),
        paymentReference= Some("paymentReference"),
        paymentAmount = Some(BigDecimal("2000.00")),
        dueDate = Some("dueDate"),
        paymentMethod = Some("paymentMethod"),
        paymentLot = Some("paymentLot"),
        paymentLotItem = Some("paymentLotItem"),
        paymentId = Some("paymentLot-paymentLotItem")
      )))
  )

  val charges2: Charge = Charge(
    taxYear = "2019",
    transactionId = "transactionId2",
    transactionDate = Some("transactionDate2"),
    `type` = Some("type2"),
    totalAmount = Some(BigDecimal("2000.00")),
    originalAmount=Some(BigDecimal("500.00")),
    outstandingAmount = Some(BigDecimal("200.00")),
    clearedAmount = Some(BigDecimal("500.00")),
		chargeType = Some("POA1"),
    mainType = Some("4920"),
    items = Some(Seq(
      SubItem(
        subItemId = Some("2"),
        amount = Some(BigDecimal("200.00")),
        clearingDate = Some("clearingDate2"),
        clearingReason = Some("clearingReason2"),
        outgoingPaymentMethod = Some("outgoingPaymentMethod2"),
        paymentReference= Some("paymentReference2"),
        paymentAmount = Some(BigDecimal("3000.00")),
        dueDate = Some("dueDate2"),
        paymentMethod = Some("paymentMethod2"),
        paymentLot = Some("paymentLot2"),
        paymentLotItem = Some("paymentLotItem2"),
        paymentId = Some("paymentLot2-paymentLotItem2")
      )))
  )

  val chargeJson: JsObject = Json.obj(
    "financialDetails" -> Json.arr(
      Json.obj(
        "taxYear" -> "2018",
        "documentId" -> "transactionId",
        "documentDate" -> "transactionDate",
        "documentDescription" -> "type",
        "originalAmount" -> 1000.00,
        "totalAmount" -> 1000.00,
        "originalAmount" -> 500.00,
        "documentOutstandingAmount" -> 500.00,
        "clearedAmount" -> 500.00,
				"chargeType" -> "POA1",
				"mainType" -> "4920",
        "items" -> Json.arr(
          Json.obj(
            "subItem" -> "1",
            "amount" -> 100.00,
            "clearingDate" -> "clearingDate",
            "clearingReason" -> "clearingReason",
            "outgoingPaymentMethod" -> "outgoingPaymentMethod",
            "paymentReference" -> "paymentReference",
            "paymentAmount" -> 2000.00,
            "dueDate" -> "dueDate",
            "paymentMethod" -> "paymentMethod",
            "paymentLot" -> "paymentLot",
            "paymentLotItem" -> "paymentLotItem"
          )
        )
      ),
      Json.obj(
        "taxYear" -> "2019",
        "documentId" -> "transactionId2",
        "documentDate" -> "transactionDate2",
        "documentDescription" -> "type2",
        "totalAmount" -> 2000.00,
        "originalAmount" -> 500.00,
        "documentOutstandingAmount" -> 200.00,
        "clearedAmount" -> 500.00,
				"chargeType" -> "POA1",
				"mainType" -> "4920",
        "items" -> Json.arr(
          Json.obj(
            "subItem" -> "2",
            "amount" -> 200.00,
            "clearingDate" -> "clearingDate2",
            "clearingReason" -> "clearingReason2",
            "outgoingPaymentMethod" -> "outgoingPaymentMethod2",
            "paymentReference" ->"paymentReference2",
            "paymentAmount" -> 3000.00,
            "dueDate" -> "dueDate2",
            "paymentMethod" -> "paymentMethod2",
            "paymentLot" -> "paymentLot2",
            "paymentLotItem" -> "paymentLotItem2"
          )
        )
      )
    )
  )

  s"GET ${controllers.routes.FinancialDetailChargesController.getChargeDetails(testNino, from, to)}" should {
    s"return $OK" when {
      "charge details are successfully retrieved" in {

        isAuthorised(true)

        stubGetChargeDetails(testNino, from, to)(
          status = OK,
          response = chargeJson)

        val res: WSResponse = IncomeTaxViewChange.getChargeDetails(testNino, from, to)

        res should have(
          httpStatus(OK),
          jsonBodyMatching(Json.toJson(ChargesResponse(List(charges1, charges2))))
        )
      }
    }

    s"return $NOT_FOUND" when {
      "an unexpected status with NOT_FOUND was returned when retrieving charge details" in {

        isAuthorised(true)

        val errorJson = Json.obj("code" -> "NO_DATA_FOUND", "reason" -> "The remote endpoint has indicated that no data can be found.")
        stubGetChargeDetails(testNino, from, to)(
          status = NOT_FOUND, response = errorJson
        )

        val res: WSResponse = IncomeTaxViewChange.getChargeDetails(testNino, from, to)

        res should have(
          httpStatus(NOT_FOUND),
          bodyMatching(errorJson.toString())
        )
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "an unexpected status was returned when retrieving charge details" in {

        isAuthorised(true)

        stubGetChargeDetails(testNino, from, to)(
          status = SERVICE_UNAVAILABLE
        )

        val res: WSResponse = IncomeTaxViewChange.getChargeDetails(testNino, from, to)

        res should have(
          httpStatus(INTERNAL_SERVER_ERROR)
        )
      }
    }
  }
}
