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

package connectors

import config.MicroserviceAppConfig
import javax.inject.{Inject, Singleton}
import models.incomeSourceDetails.{IncomeSourceDetailsError, IncomeSourceDetailsModel, IncomeSourceDetailsResponseModel}
import play.api.Logger
import play.api.http.Status
import play.api.http.Status._
import uk.gov.hmrc.http.logging.Authorization
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class IncomeSourceDetailsConnector @Inject()(val http: HttpClient,
                                    val appConfig: MicroserviceAppConfig
                                   )(implicit ec: ExecutionContext) extends RawResponseReads {

  val getIncomeSourceDetailsUrl: String => String =
    mtdRef => s"${appConfig.desUrl}/registration/business-details/mtdbsa/$mtdRef"

  def getIncomeSourceDetails(mtdRef: String)(implicit headerCarrier: HeaderCarrier): Future[IncomeSourceDetailsResponseModel] = {

    val url = getIncomeSourceDetailsUrl(mtdRef)
    val desHC = headerCarrier.copy(authorization = Some(Authorization(s"Bearer ${appConfig.desToken}")))
      .withExtraHeaders("Environment" -> appConfig.desEnvironment)

    Logger.debug(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] - Calling GET $url \n\nHeaders: $desHC")
    http.GET[HttpResponse](url)(httpReads, desHC, implicitly) map {
      response =>
        response.status match {
          case OK =>
            Logger.debug(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] - RESPONSE status:${response.status}, body:${response.body}")
            response.json.validate[IncomeSourceDetailsModel](IncomeSourceDetailsModel.desReads) fold(
              invalid => {
                Logger.error(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] - Validation Errors: $invalid")
                IncomeSourceDetailsError(Status.INTERNAL_SERVER_ERROR, "Json Validation Error. Parsing Des Business Details")
              },
              valid =>{
                Logger.info(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] successfully parsed response to LastTaxCalculation")
                valid
              }
            )
          case _ =>
            Logger.error(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] - RESPONSE status: ${response.status}, body: ${response.body}")
            IncomeSourceDetailsError(response.status, response.body)
        }
    } recover {
      case ex =>
        Logger.error(s"[IncomeSourceDetailsConnector][getIncomeSourceDetails] - Unexpected failed future, ${ex.getMessage}")
        IncomeSourceDetailsError(Status.INTERNAL_SERVER_ERROR, s"Unexpected failed future, ${ex.getMessage}")
    }
  }
}
