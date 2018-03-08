# Piggy Metrics

[The original project](https://github.com/sqshq/PiggyMetrics).

## Change List

* Upgrade Spring Cloud to [Dalston.SR4](https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-dependencies/Dalston.SR4)
* Added Zipkin and Sleuth
* Improvement of Dockerfile such as `RUN_ARGS` & `JAVA_OPTS` environment variable support
* Removed some unit tests 
* YAML template for Alauda Cloud
* Added spring profile docker for each service's spring config file.
* Added fallback class to all FeignClients
* Added spring-cloud-bus dependency to Config server for testing {config_server_ip}:9876/bus/refresh
* Added swagger-ui support for gateway. But swagger-ui for accounts service does not work, why? 

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


## Docker Images working with Alauda platform

* index.alauda.cn/claas/piggy-new-web
* index.alauda.cn/claas/piggy-new-auth-service
* index.alauda.cn/claas/piggy-new-account-service
* index.alauda.cn/claas/piggy-new-statistics-service
* index.alauda.cn/claas/piggy-new-notification-service


## Sample YAML for Alauda Cloud
[Alauda.yaml](./alauda.yml)

## CL for testing images with docker

`docker run -d -p 15672:15672 -p 5672:5672  --name rabbitmq rabbitmq:3-management`

`docker run -d -p 27017:27017 \
--env INIT_DUMP="account-service-dump.js" \
--env MONGODB_PASSWORD="admin" \
--name data-mongodb index.alauda.cn/demo100/piggymetrics-mongodb`

`docker run -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --env ES_JAVA_OPTS='-Xms64m -Xmx64m' --name elasticsearch docker.elastic.co/elasticsearch/elasticsearch:5.6.1`


`docker run --rm -p 8888:8888 --env CONFIG_SERVICE_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--env ALAUDA_GIT="https://github.com/PiggyMetrics/AppConfig.git" \
--env ALAUDA_GIT_USER="PiggyMetrics" --env ALAUDA_GIT_PASSWORD="alauda1234" \
--link rabbitmq:rabbitmq \
--name config   index.alauda.cn/claas/piggy-config`
`curl http://user:admin@localhost:8888/auth_service-docker.yml`


`docker run -d -p 8761:8761  --env CONFIG_SERVICE_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --name registry  index.alauda.cn/claas/piggy-registry`
`http://localhost:8761`

`docker run --rm -p 4000:4000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--name gateway  index.alauda.cn/claas/piggy-gateway`

http://localhost:4000/swagger-ui.html

`docker run --rm -p 5000:5000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env NOTIFICATION_SERVICE_PASSWORD="admin" \
--env STATISTICS_SERVICE_PASSWORD="admin" \
--env ACCOUNT_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:auth-mongodb \
--link rabbitmq:rabbitmq \
--name auth-service index.alauda.cn/claas/piggy-auth-service`

`docker run --rm -p 6000:6000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env ACCOUNT_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:account-mongodb \
--link auth-service:auth-service \
--link rabbitmq:rabbitmq \
--name account-service index.alauda.cn/claas/piggy-account-service`

`docker run --rm -p 7000:7000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env STATISTICS_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:statistics-mongodb \
--link rabbitmq:rabbitmq \
--link auth-service:auth-service \
--name statistics-service index.alauda.cn/claas/piggy-statistics-service`

`docker run --rm -p 8000:8000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env NOTIFICATION_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:notification-mongodb \
--link rabbitmq:rabbitmq \
--link auth-service:auth-service \
--name notification-service index.alauda.cn/claas/piggy-notification-service`

`docker run --rm -p 8989:8989 -p 8080:8080 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link rabbitmq:rabbitmq \
--name monitoring index.alauda.cn/claas/piggy-monitoring`
`http://localhost:8080/hystrix`

`docker run --rm -p 9411:9411 \
--env CONFIG_SERVICE_PASSWORD="admin" \_``_````
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link rabbitmq:rabbitmq --link elasticsearch:elasticsearch \
--name zipkin index.alauda.cn/claas/piggy-zipkin`
`http://localhost:9411`

