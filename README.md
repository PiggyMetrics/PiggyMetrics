# Piggy Metrics

[The original project](https://github.com/sqshq/PiggyMetrics).

## Change List

* Upgrade Spring Cloud to [Dalston.SR4](https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies/Dalston.SR4)
* Added Zipkin 
* Improvement of Dockerfile such as `RUN_ARGS` & `JAVA_OPTS` environment variable support
* Removed unit tests temporarily 
* YAML template for Alauda Cloud
* Added spring profile docker for each service's spring config file.

## Docker Images 

* index.alauda.cn/claas/piggy-rabbitmq:3-management
* index.alauda.cn/claas/piggy-mongodb
	* shipped with init scripts
* index.alauda.cn/claas/piggy-elasticsearch:update
	* single node mode
	* Zipkin dependency
* index.alauda.cn/claas/piggy-config
* index.alauda.cn/claas/piggy-registry
* index.alauda.cn/claas/piggy-gateway
* index.alauda.cn/claas/piggy-auth-service
* index.alauda.cn/claas/piggy-account-service
* index.alauda.cn/claas/piggy-statistics-service
* index.alauda.cn/claas/piggy-notification-service
* index.alauda.cn/claas/piggy-monitoring
* index.alauda.cn/claas/piggy-zipkin


