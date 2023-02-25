# freelance_platform
freelance_platform is a demo application.The platform acts as an interface for users who are hiring freelancer or finding freelance jobs in Hong Kong.
The platform can filter the job based on the districts and apply for their jobs.While the user can post a job , hire and choose for the best freelancer
for their jobs.A Simple chatroom will also associated for each job


# Backend Server:
The backend of the platform applied the springboot microservice architecture with docker.The reactive and scalable features allowed platform handle clients 'requests 
more efficiently.The server also applied api-gateway for routing and securing the server by oauth2 resource server and json web token in spring webflux security.
The application also applied service eureka server, kafka messaging queue ,zipkin ,mongodb and mysql.All of these are shown in docker-compose.yml file.


# Get start with the api
Since the server is secured by jwt, apis other than sign in and sign out involve jwt to authorize the server

sign up:
browsing "/signup " with signUpDto.class:

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

signin:
browsing "/signin" with basic auth

Response:
{
    "user_id": $USER_ID(INT)
    "token": "$TOKEN"
}

the token can last for 3 hours and each can only access with jwt by adding bearer token with authorization at the header

# Microservice architectire:
    
