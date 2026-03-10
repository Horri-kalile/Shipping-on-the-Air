User Service

1. Purpose

The UserService manages users in the drone delivery system.
It handles:
User registration and account management
Updating user info (email, password)
Deleting users
Providing user info and history to other services via events

2. Domain

    Entity
User
userId → unique identifier
name
email
password
whenCreated → account creation date

    Value Objects
UserId
Email
Password
UserInfo → name, email, etc.

3. DTOs
   ChangePasswordRequestDTO	Request to change password
   DeliveryRequestDTO	info for delivery request
   DeliveryStatusDTO	Show delivery status to user
   EmailUpdatedResponseDTO	
   PasswordChangedResponseDTO	
   UpdateEmailRequestDTO	info to update email
   UserResponseDTO

4. Events
   UserRegisteredEvent	
   UserEmailUpdatedEvent	
   UserDeletedEvent	

5. Communication

DeliveryService → sends and receives delivery requests/status for the user

API → gets DTOs like UserResponseDTO, PasswordChangedResponseDTO

6. Typical Flow

User registers → UserRegisteredEvent published
User updates email → UserEmailUpdatedEvent published
User changes password → returns PasswordChangedResponseDTO
User creates a delivery → sends DeliveryRequestDTO to DeliveryService