# SpringBoot_Chat

## WebSocket Integration
### Initially this project started with the integration of a websocket without using any predefined STOMP tools from the Spring ecosystem. This meant adding users to channels, getting messages and Sending messages where all done distinctly with loops checking each users validity and sending the message over the socket through JSON, as opposed to using standard text.

### As this project evolved it then implemented the STOMP framework for websocket configuration where data would now be pushed to endpoints where data could be sent and received by each user and both users could see the message in a JSON format with the users name, message and the timestamp of when it was sent.

## PostgreSQL Usage
### Used for data persistnece across Instances. Created a users and messages table that allowed for that information to be stored. Used Flyway for migrations on start up. Used REST endpoints to fetch message history

## WebSocket Authentication
### Authentication was done through JWTs(JSON Web Tokens) where each user would be assigned a token on login. This was used to authenticate users and allow for more security in communication across users. Role based access was given using #PreAuthroiezed to make an admin only feature where an admin could delete any user. If delete was called by a regular user it returned a 403(Unauthorized action at the endpoint) and the admin would get a 204 meaning it was sucessful and there was no contennt found anymore at the endpoint

## Redis Implementation
### The first task that I undertook with Redis was the usage of its pub/sub capabilties to be able to create a more freefrom style of messaging. This allowed for users to post their message into a channel and then there would be a listener on teh channel to ensure that the messages were picked up and seen by others. In addtion to this, there was prescence tracking to track all users who were on an app instance at that time.

### In additon I used Redis to be able to implement rate limiting for each user, which would have timer set on the cache with an allowed amount of messages sent by each user in that time. ANy messages that exceeded the rate would not be saved into Postgres. Adding more layers of security to be able to stop attackers from flooding endpoints with requests and to allow for the app to better handle higher loads of traffic. 

## Containerization
### Using Dockerfiles to build each component up from the app. This used docker compose to be able to spin up instances for our postgres channels, Redis and the application. In addition to this there were Apring Actuator health chekcs used to check the    current status of each instance 

## Kubernetes
### The entire application with containerization was then deployed on a local minikube cluster that handled creation of replica clusters, services with addressing to each internal service in the cluster, secrets used for kubernetes configuration and internal probes that checked for health and liveliness of features within the cluster.

## Pseudo AWS deployment using LocalStack
### Used LocalStack as a local emulator that was able to run S3, IAM and ECR with ECS task definition with the deployment of Lambda and Cloudwatch. S3 instances were used as object buckets to store information about the service. IAM was used for authroization allowing for readonly policies to be created for users allowing for granting certain permissions to users. Then ECR(Elastic Container Registry) was used to push the chatserver image to the registry. Lambda and Cloudwatch were used to create logs for the app to watch and maintain the status and health of the service.