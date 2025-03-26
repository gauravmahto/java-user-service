# java-user-service
Java microservice - User service for authentication using JWT

Client
 │
 ├───► User Service (Authenticate) ────► JWT Token
 │
 └───► API Gateway ───► Product / Order Service
           │ (verify JWT token clearly here)
