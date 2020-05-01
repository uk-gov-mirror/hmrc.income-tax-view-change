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

package services

import assets.BaseTestConstants._
import assets.ReportDeadlinesTestConstants._
import connectors.ReportDeadlinesConnector
import models.reportDeadlines.{ObligationsModel, ReportDeadlinesResponseModel}
import org.mockito.ArgumentMatchers.{any, eq => matches}
import org.mockito.Mockito.when
import utils.TestSupport

import scala.concurrent.Future

class ReportDeadlinesServiceSpec extends TestSupport {

  trait Setup {
    val reportDeadlinesConnector: ReportDeadlinesConnector = mock[ReportDeadlinesConnector]
    val service: ReportDeadlinesService = new ReportDeadlinesService(reportDeadlinesConnector)
  }

  "getReportDeadlines" should {
    "return obligations retrieved from the connector" when {

      "they match the nino" in new Setup {
        when(reportDeadlinesConnector.getReportDeadlines(matches(testNino), matches(true))(any()))
          .thenReturn(Future.successful(Right(ObligationsModel(Seq(testReportDeadlines_4)))))

        val result: ReportDeadlinesResponseModel = await(service.getReportDeadlines(testNino, openObligations = true))

        result shouldBe ObligationsModel(Seq(testReportDeadlines_4))
      }
    }

    "return an error model" when {
      "the obligations returned from the connector do not match the nino" in new Setup {
        when(reportDeadlinesConnector.getReportDeadlines(matches("notfoundnino"), matches(true))(any()))
          .thenReturn(Future.successful(Right(testObligations)))

        val result: ReportDeadlinesResponseModel = await(service.getReportDeadlines("notfoundnino", openObligations = true))

        result shouldBe testObligations
      }

      "the connector returned an error model" in new Setup {
        when(reportDeadlinesConnector.getReportDeadlines(matches(testNino), matches(true))(any()))
          .thenReturn(Future.successful(Left(testReportDeadlinesError)))

        val result: ReportDeadlinesResponseModel = await(service.getReportDeadlines(testNino, openObligations = true))

        result shouldBe testReportDeadlinesError
      }
    }
  }

}
