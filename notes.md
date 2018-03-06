
## Environment Variables When testing
<pre>
export CONFIG_SERVICE_PASSWORD=admin
export NOTIFICATION_SERVICE_PASSWORD=admin
export STATISTICS_SERVICE_PASSWORD=admin
export ACCOUNT_SERVICE_PASSWORD=admin
export MONGODB_PASSWORD=admin
</pre>

## Statements to debug the services

### Get the authorization token for UI webapp
* through gateway to token
<pre>
curl -H 'Authorization: Basic YnJvd3Nlcjo=' \ 
     -d 'scope=ui&username=haitao&password=haitao&grant_type=password' \
     http://localhost:4000/uaa/oauth/token
</pre>

* call auth-service directly
<pre>
curl -H 'Authorization: Basic YnJvd3Nlcjo=' http://localhost:5000/uaa/oauth/token -d 'scope=ui&username=haitao&password=haitao&grant_type=password' 
</pre>

* call auth-service directly with explicit credential.
<pre>
curl -d 'scope=ui&username=haitao&password=haitao&grant_type=password' \
 http://browser@localhost:5000/uaa/oauth/token 
</pre>

 
### Get the authorization token for account service
<pre>
curl http://account-service:admin@localhost:5000/uaa/oauth/token \
 -d 'scope=server&username=account-service&password=admin&grant_type=client_credentials'

{"access_token":"430097d5-6ebe-4c77-88f3-3a2ea7b06bac","token_type":"bearer","expires_in":43199,"scope":"server"}%
</pre>

### Call account service
<pre>
curl http://localhost:6000/accounts/current -H "Authorization: Bearer 94bd7450-c71d-4646-8fc4-cd3d1079e43b"
</pre>
<pre>
curl http://localhost:6000/accounts/v2/api-docs -v -H "Authorization: Bearer 89989c76-1b94-495f-8c71-6cdf4fbd6383"
</pre>

### Call auth service to create a new user
<pre>
curl -v http://localhost:5000/uaa/users \
-H "Authorization: Bearer 75ed7b6a-239b-4db8-8255-a763129a4fb4" \
-H "Content-Type: application/json" \
-d '{ "username": "a234", "password": "vbnfgh"}' 
</pre>

### Call account service to create a new user, No authentication is required, why? 
<pre>
curl http://192.168.0.76:6000/accounts/ -v \
-d '{"username": "a456", "password": "vbnfgh"}' \
-H "Content-Type: application/json"
</pre>

### Call other services
<pre>
curl http://localhost:8000/notifications/recipients/current \
-H "Authorization: Bearer 49836fae-b150-4e60-81d2-094305cd8bf8" \
-H "Content-Type: application/json"
</pre>

<pre>
curl http://localhost:7000/statistics/current \
-H "Authorization: Bearer 49836fae-b150-4e60-81d2-094305cd8bf8" \
-H "Content-Type: application/json"
</pre>
 
## How to build projects 
mvn clean package -DskipTests=true
 

## Build docker images
<pre>
docker build -t index.alauda.cn/claas/piggy-config .
docker build -t index.alauda.cn/claas/piggy-registry .
docker build -t index.alauda.cn/claas/piggy-gateway .
docker build -t index.alauda.cn/claas/piggy-auth-service .
docker build -t index.alauda.cn/claas/piggy-account-service .
docker build -t index.alauda.cn/claas/piggy-statistics-service .
docker build -t index.alauda.cn/claas/piggy-notification-service .
docker build -t index.alauda.cn/claas/piggy-monitoring .
docker build -t index.alauda.cn/claas/piggy-zipkin .
</pre>

And dependency images

* This image can be replaced by official image
<pre>
index.alauda.cn/claas/piggy-rabbitmq:3-management 
</pre>

* This image include init scripts
<pre>
index.alauda.cn/claas/piggy-mongodb
</pre>

* This ES is single-node mode only
<pre>
index.alauda.cn/claas/piggy-elasticsearch:update
</pre>


## Docker run statements for testing 
### Config server
<pre>
docker run --rm -p 8888:8888 --env CONFIG_SERVICE_PASSWORD=admin \
--env RUN_ARGS="--spring.profiles.active=docker --CONFIG_SERVICE_PASSWORD=admin --ALAUDA_GIT=http://139.219.58.41:9999/root/AppConfig.git --ALAUDA_GIT_USER=root --ALAUDA_GIT_PASSWORD=alauda1234" \
--name config index.alauda.cn/piggy-metrics/config 
</pre>

### Check the config server
`curl http://user:admin@localhost:8888/registry-docker.yml`

### Registry 
<pre>
docker run --rm -p 8761:8761 \
--env RUN_ARGS="--spring.profiles.active=docker --CONFIG_SERVICE_PASSWORD=admin" \
--link config:config \
--name registry index.alauda.cn/piggy-metrics/registry 
</pre>
 
### Gateway
<pre>
docker run --rm -p 4000:4000 \
--env RUN_ARGS="--spring.profiles.active=docker --CONFIG_SERVICE_PASSWORD=admin" \
--link config:config --link registry:registry \
--name gateway index.alauda.cn/piggy-metrics/gateway 
</pre>

### Account service
<pre>
docker run --rm -p 9411:9411 --env CONFIG_SERVICE_PASSWORD=admin \
--env RUN_ARGS="--spring.profiles.active=docker" \
--link config:config \
--name zipkin index.alauda.cn/piggy-metrics/zipkin
</pre>
