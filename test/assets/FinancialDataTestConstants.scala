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

package assets


import models.financialDetails.{Charge, SubItem}
import play.api.libs.json.{JsObject, JsValue, Json}
import uk.gov.hmrc.http.HttpResponse

object FinancialDataTestConstants {

  val validChargeJson: JsValue = Json.parse(
    """
      |{
      |     "taxYear": "2018",
      |     "documentId": "transactionId",
      |     "documentDate": "transactionDate",
      |     "documentDescription": "type",
      |     "totalAmount": 1000.00,
      |     "originalAmount": 500.00,
      |     "clearedAmount": 500.00,
      |     "documentOutstandingAmount": 500.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItem": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem"
      |       }
      |     ]
      |}
      |""".stripMargin)


  val validChargesJson: JsValue = Json.parse(
    """
      |{
      | "financialDetails": [
      |   {
      |     "taxYear": "2018",
      |     "documentId": "transactionId",
      |     "documentDate": "transactionDate",
      |     "documentDescription": "type",
      |     "totalAmount": 1000.00,
      |     "originalAmount": 500.00,
      |     "clearedAmount": 500.00,
      |     "documentOutstandingAmount": 500.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItem": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem"
      |       }
      |     ]
      |   },
      |   {
      |     "taxYear": "2019",
      |     "documentId": "transactionId2",
      |     "documentDate": "transactionDate2",
      |     "documentDescription": "type2",
      |     "totalAmount": 2000.00,
      |     "originalAmount": 500.00,
      |     "clearedAmount": 500.00,
      |     "documentOutstandingAmount": 200.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItem": "2",
      |       "amount": 200.00,
      |       "clearingDate": "clearingDate2",
      |       "clearingReason": "clearingReason2",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod2",
      |       "paymentReference": "paymentReference2",
      |       "paymentAmount": 3000.00,
      |       "dueDate": "dueDate2",
      |       "paymentMethod": "paymentMethod2",
      |       "paymentLot": "paymentLot2",
      |       "paymentLotItem": "paymentLotItem2"
      |       }
      |     ]
      |   }
      | ]
      |}
      |""".stripMargin)


  val validChargeJsonAfterWrites: JsValue = Json.parse(
    """
      |{
      |     "taxYear": "2018",
      |     "transactionId": "transactionId",
      |     "transactionDate": "transactionDate",
      |     "type": "type",
      |     "totalAmount": 1000.00,
      |     "originalAmount": 500.00,
      |     "outstandingAmount": 500.00,
      |     "clearedAmount": 500.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItemId": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem",
      |       "paymentId": "paymentLot-paymentLotItem"
      |       }
      |     ]
      |}
      |""".stripMargin)

  val validChargesJsonAfterWrites: JsValue = Json.parse(
    """
      |{
      | "financialDetails": [
      |   {
      |     "taxYear": "2018",
      |     "transactionId": "transactionId",
      |     "transactionDate": "transactionDate",
      |     "type": "type",
      |     "totalAmount": 1000.00,
      |     "originalAmount": 500.00,
      |     "outstandingAmount": 500.00,
      |     "clearedAmount": 500.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItemId": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem",
      |       "paymentId": "paymentLot-paymentLotItem"
      |       }
      |     ]
      |   },
      |   {
      |     "taxYear": "2019",
      |     "transactionId": "transactionId2",
      |     "transactionDate": "transactionDate2",
      |     "type": "type2",
      |     "totalAmount": 2000.00,
      |     "originalAmount": 500.00,
      |     "outstandingAmount": 200.00,
      |     "clearedAmount": 500.00,
      |     "chargeType": "POA1",
      |     "mainType": "4920",
      |     "items": [{
      |       "subItemId": "2",
      |       "amount": 200.00,
      |       "clearingDate": "clearingDate2",
      |       "clearingReason": "clearingReason2",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod2",
      |       "paymentReference": "paymentReference2",
      |       "paymentAmount": 3000.00,
      |       "dueDate": "dueDate2",
      |       "paymentMethod": "paymentMethod2",
      |       "paymentLot": "paymentLot2",
      |       "paymentLotItem": "paymentLotItem2",
      |       "paymentId": "paymentLot2-paymentLotItem2"
      |       }
      |     ]
      |   }
      | ]
      |}
      |""".stripMargin)

  val validPaymentsJsonAfterWrites: JsValue = Json.parse(
    """
      |[
      | {
      |   "reference": "paymentReference",
      |   "amount": 2000.00,
      |   "method": "paymentMethod",
      |   "lot": "paymentLot",
      |   "lotItem": "paymentLotItem",
      |   "date": "clearingDate"
      | },
      | {
      |   "reference": "paymentReference2",
      |   "amount": 3000.00,
      |   "method": "paymentMethod2",
      |   "lot": "paymentLot2",
      |   "lotItem": "paymentLotItem2",
      |   "date": "clearingDate2"
      | }
      |]
      |""".stripMargin)


  val testChargeHttpResponse: HttpResponse = HttpResponse(200, responseJson = Some(validChargesJson))
  val testEmptyChargeHttpResponse: HttpResponse = HttpResponse(200, responseJson = Some(Json.toJson("")))

  val charges1: Charge = Charge(
    taxYear = "2018",
    transactionId = "transactionId",
    transactionDate = Some("transactionDate"),
    `type` = Some("type"),
    totalAmount = Some(BigDecimal("1000.00")),
    originalAmount = Some(BigDecimal(500.00)),
    outstandingAmount = Some(BigDecimal("500.00")),
    clearedAmount = Some(BigDecimal(500.00)),
		chargeType = Some("POA1"),
    mainType = Some("4920"),
    items = Some(Seq(
      SubItem(
        subItemId = Some("1"),
        amount = Some(BigDecimal("100.00")),
        clearingDate = Some("clearingDate"),
        clearingReason = Some("clearingReason"),
        outgoingPaymentMethod = Some("outgoingPaymentMethod"),
        paymentReference = Some("paymentReference"),
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
    originalAmount = Some(BigDecimal(500.00)),
    outstandingAmount = Some(BigDecimal("200.00")),
    clearedAmount = Some(BigDecimal(500.00)),
		chargeType = Some("POA1"),
    mainType = Some("4920"),
    items = Some(Seq(
      SubItem(
        subItemId = Some("2"),
        amount = Some(BigDecimal("200.00")),
        clearingDate = Some("clearingDate2"),
        clearingReason = Some("clearingReason2"),
        outgoingPaymentMethod = Some("outgoingPaymentMethod2"),
        paymentReference = Some("paymentReference2"),
        paymentAmount = Some(BigDecimal("3000.00")),
        dueDate = Some("dueDate2"),
        paymentMethod = Some("paymentMethod2"),
        paymentLot = Some("paymentLot2"),
        paymentLotItem = Some("paymentLotItem2"),
        paymentId = Some("paymentLot2-paymentLotItem2")
      )))
  )

  val validSubItemJson: JsValue = Json.parse(
    """
      |{
      |       "subItem": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem"
      |}
      |""".stripMargin)


  val subItems1: SubItem = SubItem(
    subItemId = Some("1"),
    amount = Some(BigDecimal("100.00")),
    clearingDate = Some("clearingDate"),
    clearingReason = Some("clearingReason"),
    outgoingPaymentMethod = Some("outgoingPaymentMethod"),
    paymentReference = Some("paymentReference"),
    paymentAmount = Some(BigDecimal("2000.00")),
    dueDate = Some("dueDate"),
    paymentMethod = Some("paymentMethod"),
    paymentLot = Some("paymentLot"),
    paymentLotItem = Some("paymentLotItem"),
    paymentId = Some("paymentLot-paymentLotItem")
  )

  val validSubItemJsonAfterWrites: JsValue = Json.parse(
    """
      |{
      |       "subItemId": "1",
      |       "amount": 100.00,
      |       "clearingDate": "clearingDate",
      |       "clearingReason": "clearingReason",
      |       "outgoingPaymentMethod": "outgoingPaymentMethod",
      |       "paymentReference": "paymentReference",
      |       "paymentAmount": 2000.00,
      |       "dueDate": "dueDate",
      |       "paymentMethod": "paymentMethod",
      |       "paymentLot": "paymentLot",
      |       "paymentLotItem": "paymentLotItem",
      |       "paymentId": "paymentLot-paymentLotItem"
      |}
      |""".stripMargin)

}
