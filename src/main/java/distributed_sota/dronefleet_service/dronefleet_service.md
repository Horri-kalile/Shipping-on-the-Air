drone_fleet Service

1. Purpose

Handling drone assignement, base location, and location of drone

2. Domain

        Aggregate

Base
Drone
 
        Value Objects
BaseId
Battery
DroneId
Location

3. DTOs
   BaseCreatedResponseDTO
   BaseInfoDTO
   DroneAssignmentRequestDTO
   DroneAssignmentResponseDTO
   DroneCreatedResponseDTO
   DroneInfoDTO

4. Events
   DroneAssignedEvent
   DroneAtPickupEvent
   DroneBackedToBaseEvent
   DroneCannotBeAssignedEvent
   DroneDeliveredEvent
   DroneEvent
   DroneLeftBaseEvent
   DroneLocationUpdatedEvent
   DroneMaintenanceScheduledEvent
   DroneReturnedToBaseEvent

5. Communication

DeliveryService → receive drone request + send drone position
Trackingservice

6. Typical Flow

DeliveryService request HTTP -> drone handling
drone service ask/get drone position -> Tracking (calculate position continiously) 