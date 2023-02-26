# Backend Server:
The backend of the platform applied the springboot microservice architecture with docker.The reactive and scalable features allowed platform handle clients 'requests 
more efficiently.The server also applied api-gateway for routing and securing the server by oauth2 resource server and json web token in spring webflux security.
The application also applied service eureka server, kafka messaging queue ,zipkin ,mongodb and mysql.All of these are shown in docker-compose.yml file.
The apis are tested with integration test and unit test


# Get start with the api
Since the server is secured by jwt, apis other than sign in and sign out involve jwt to authorize the server

sign up:
browsing "/signup " with signUpDto.class:

```
{
   "username": "$USERNAME",
   "password": "$PASSWORD",
   "fullname": "$FULLNAME",
   "email","$EMAIL",
   "skill_set": "$SKILL_SET",
   "contact": "$CONTACT",
   "cv": "$CV",
   "address_id": $ADDRESS_ID(INT)
}
```

signin:
browsing "/signin" with basic auth

```
Response:
{
    "user_id": $USER_ID(INT)
    "token": "$TOKEN"
}
```



the token can last for 3 hours and each can only access with jwt by adding bearer token with authorization at the header

# Microservice architectire:
1.api-gateway:
- service for authentication routing the apis to the corresponding service
- applying web security with jwt and oauth2 resource server
- webflux reactive programming

2.eureka-server:
- service for identify and mapping of service
- spring cloud discovery server

3.userjob:
- main service for handling jobs,users,applications
- mysql

4.chatroom
- service for chatroom base on the job
- mongodb
- reactive programming

5.notification
- service for listening asychronous notification
- spring cloud kafka messaging queue
- mongodb
