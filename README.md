# Backend Server:
The backend of the platform applied the springboot microservice architecture with docker.The reactive and scalable features allowed platform handle clients 'requests 
more efficiently.The server also applied api-gateway for routing and securing the server by oauth2 resource server and json web token in spring webflux security.
The application also applied service eureka server, kafka messaging queue ,zipkin ,mongodb and mysql.All of these are shown in docker-compose.yml file.
The apis are tested with integration test and unit test


# Get start with the api
Since the server is secured by jwt, apis other than sign in and sign out involve jwt to authorize the server

- Quick sign in   :
```
curl -X POST http://sprin-LoadB-KBBLNWRJLMKX-110df375c0b3a71d.elb.us-west-2.amazonaws.com:8080/signin -u admin:password
```
Above sign in with username:admin , password:password

AuthResponse (expected response):
```
{
    "user_id": $USER_ID(INT)
    "token": "$TOKEN"
}
```
token contain user's credentials , generated time ,expired time , and some secret


- Using token to access apis:

the below api responsible for showing all available jobs can be applied for freelancer and the token form .
Using the token generated from the authresponse and replace it with the $TOKEN below .
```
curl http://sprin-LoadB-KBBLNWRJLMKX-110df375c0b3a71d.elb.us-west-2.amazonaws.com:8080/UserJob/jobs/all -H "Authorization: Bearer $TOKEN"

``` 



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
