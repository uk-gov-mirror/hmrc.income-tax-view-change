# Income Tax View and Change Microservice

[![Build Status](https://travis-ci.org/hmrc/income-tax-view-change.svg)](https://travis-ci.org/hmrc/income-tax-view-change) [ ![Download](https://api.bintray.com/packages/hmrc/releases/income-tax-view-change/images/download.svg) ](https://bintray.com/hmrc/releases/income-tax-view-change/_latestVersion)

This is the protected backend for the Quarterly Reporting Service (MTD ITSA). 

Frontend: https://github.com/hmrc/income-tax-view-change-frontend
Stub: https://github.tools.tax.service.gov.uk/hmrc/itvc-dynamic-stub

Requirements
------------

This service is written in [Scala](http://www.scala-lang.org/) and [Play](http://playframework.com/), so needs at least a [JRE] to run.


## Run the application


To update from Nexus and start all services from the RELEASE version instead of snapshot

```
sm --start ITVC_ALL -f
```


### To run the application locally execute the following:

Kill the service ```sm --stop INCOME_TAX_VIEW_CHANGE``` and run:
```
sbt 'run 9082'
```



## Test the application

To test the application execute

```
sbt test
```



### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")
