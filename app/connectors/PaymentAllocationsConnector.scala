/*
 * Copyright 2020 HM Revenue & Customs
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

package connectors

import config.MicroserviceAppConfig
import connectors.httpParsers.PaymentAllocationsHttpParser.{PaymentAllocationsReads, PaymentAllocationsResponse}
import javax.inject.Inject
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class PaymentAllocationsConnector @Inject()(val http: HttpClient,
                                            val appConfig: MicroserviceAppConfig)
                                           (implicit ec: ExecutionContext) extends RawResponseReads {

  private[connectors] def desHeaderCarrier(implicit hc: HeaderCarrier): HeaderCarrier = {
    hc.copy(authorization = Some(Authorization(s"Bearer ${appConfig.desToken}")))
      .withExtraHeaders("Environment" -> appConfig.desEnvironment)
  }

  private[connectors] def paymentAllocationsUrl(nino: String): String = {
    s"${appConfig.desUrl}/cross-regime/payment-allocation/NINO/$nino/ITSA"
  }

  private[connectors] def queryParameters(paymentLot: String, paymentLotItem: String): Seq[(String, String)] = {
    Seq(
      "paymentLot" -> paymentLot,
      "paymentLotItem" -> paymentLotItem
    )
  }

  def getPaymentAllocations(nino: String, paymentLot: String, paymentLotItem: String)
                           (implicit hc: HeaderCarrier): Future[PaymentAllocationsResponse] = {
    http.GET(
      url = paymentAllocationsUrl(nino),
      queryParams = queryParameters(paymentLot, paymentLotItem)
    )(PaymentAllocationsReads, desHeaderCarrier, ec)
  }

}