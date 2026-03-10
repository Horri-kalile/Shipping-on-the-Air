Delivery Service

1. Purpose

Creating, tracking, and managing deliveries
Updating delivery status and ETA
Publishing events to notify other services

2. Domain

   Aggregate

Delivery

    Value Objects

DeliveryId
DeliveryStatus
Location 
Weight
Distance 
Price
ETA 
RemainingDuration

3. DTOs
   DeliveryRequestDTO	info receive from user_service
   DeliveryStatusDTO	Shows current status of a delivery
   RemainingDurationDTO	Estimated time remaining for a delivery
   DroneAssignmentRequestDTO	Request to assign a drone to a delivery
   DroneAssignmentResponseDTO	Response with assigned drone info from drone_service
   PaymentRequestDTO	Request to process delivery payment
   PaymentResponseDTO	Response with payment result from payment_service
   PaymentStateDTO	Shows current payment state
   DeliveryDTO	General delivery info for queries

4. Events
   DeliveryCreatedEvent	
   DroneRequestedEvent	
   DeliveryStartedEvent
   EtaUpdatedEvent	
   DeliveryCompletedEvent	
   DeliveryFailedEvent	
   DeliveryCanceledEvent	
   WaitForPickUpEvent

5. Communication

UserService → receives user info for deliveries
DroneService → assigns drones and tracks progress
PaymentService → processes payments and refunds
(TelemetryService → monitors delivery events)

6. Typical Flow

User requests a delivery → DeliveryRequestDTO received
Delivery created → DeliveryCreatedEvent published
Payment processed → PaymentRequestDTO sent → PaymentResponseDTO received
Drone assigned → DroneAssignmentRequestDTO sent → DroneAssignmentResponseDTO received
Delivery in progress → updates sent via DeliveryStatusDTO and RemainingDurationDTO
Delivery completed or failed → DeliveryCompletedEvent or DeliveryFailedEvent published