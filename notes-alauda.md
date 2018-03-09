

<pre>
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic baeldung
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 5 --topic partitioned
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic filtered
bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic greeting

bin/kafka-topics --create --topic test --replication-factor 1 --partitions 1 --zookeeper localhost:2181

bin/kafka-console-producer --topic test --broker-list localhost:9092
bin/kafka-console-consumer --topic test --from-beginning --bootstrap-server localhost:9092
<pre>




<pre>
docker run -d -p 27017:27017 \
--env INIT_DUMP="account-service-dump.js" \
--env MONGODB_PASSWORD="admin" \
--network kafka-net \
--name data-mongodb index.alauda.cn/demo100/piggymetrics-mongodb
<pre>

<pre>
docker run -d -p 9092:9092 --name kafka --network kafka-net \
--hostname kafka \
 --env ZOOKEEPER_IP=zk \
--env KAFKA_ADVERTISED_HOST_NAME=kafka \
ches/kafka
<pre>


<pre>
docker run --rm -p 8888:8888 --env CONFIG_SERVICE_PASSWORD=admin \
--env RUN_ARGS="--spring.profiles.active=docker --CONFIG_SERVICE_PASSWORD=admin --ALAUDA_GIT=http://23.102.227.187:9988/root/AppConfig-common.git --ALAUDA_GIT_USER=root --ALAUDA_GIT_PASSWORD=alauda1234" \
 --network kafka-net \
--name config index.alauda.cn/piggy-metrics/config 
<pre>

<pre>
docker run --rm -p 8761:8761 \
--env RUN_ARGS="--spring.profiles.active=docker --CONFIG_SERVICE_PASSWORD=admin" \
--link config:config \
--network kafka-net \
--name registry index.alauda.cn/piggy-metrics/registry 
<pre>

<pre>
docker run --rm -p 4000:4000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--name gateway --network kafka-net index.alauda.cn/claas/piggy-gateway
<pre>

<pre>
docker run --rm -p 5000:5000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env NOTIFICATION_SERVICE_PASSWORD="admin" \
--env STATISTICS_SERVICE_PASSWORD="admin" \
--env ACCOUNT_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:auth-mongodb \
--link zk:zk \
--link kafka:kafka \
--network kafka-net \
--name auth-service index.alauda.cn/claas/piggy-new-auth-service
<pre>

<pre>
docker run --rm -p 6000:6000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env ACCOUNT_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:account-mongodb \
--link auth-service:auth-service \
--link zk:zk \
--link kafka:kafka \
--network kafka-net \
--name account-service index.alauda.cn/claas/piggy-new-account-service
<pre>

<pre>
docker run --rm -p 7000:7000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env STATISTICS_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:statistics-mongodb \
--link zk:zk \
--link kafka:kafka \
--network kafka-net \
--link auth-service:auth-service \
--name statistics-service index.alauda.cn/claas/piggy-new-statistics-service
<pre>

<pre>
docker run --rm -p 8000:8000 \
--env CONFIG_SERVICE_PASSWORD="admin" \
--env NOTIFICATION_SERVICE_PASSWORD="admin" \
--env MONGODB_PASSWORD="admin" \
--env RUN_ARGS="--spring.profiles.active=docker " \
--link config:config --link registry:registry \
--link data-mongodb:notification-mongodb \
--link zk:zk \
--link kafka:kafka \
--link auth-service:auth-service \
--network kafka-net \
--name notification-service index.alauda.cn/claas/piggy-new-notification-service
<pre>


<pre>
docker run --rm -p 8080:8080 --name webui --network kafka-net index.alauda.cn/claas/piggy-new-web
<pre>


<pre>
curl http://account-service:admin@localhost:8080/uaa/oauth/token \
 -d 'scope=server&username=account-service&password=admin&grant_type=client_credentials'
<pre>

<pre>
curl -v http://localhost:5000/uaa/users \
-H "Authorization: Bearer 7bc40b97-c280-404d-b4a5-07943de11daf" \
-H "Content-Type: application/json" \
-d '{ "username": "a234", "password": "vbnfgh"}' 
<pre>

<pre>
curl -H 'Authorization: Basic YnJvd3Nlcjo=' \ 
     -d 'scope=ui&username=a234&password=vbnfgh&grant_type=password' \
     http://localhost:4000/uaa/oauth/token
<pre>

<pre>
curl -H 'Authorization: Basic YnJvd3Nlcjo=' -d 'scope=ui&username=a234&password=vbnfgh&grant_type=password' http://localhost:8080/uaa/oauth/token
<pre>


<pre>
curl http://127.0.0.1:6000/accounts/ -v \
-d '{"username": "a456", "password": "vbnfgh"}' \
-H "Content-Type: application/json"
<pre>

