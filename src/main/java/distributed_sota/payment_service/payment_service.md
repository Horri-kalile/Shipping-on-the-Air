Payment Service

1. Purpose

Creating handling payment request

2. Domain

   Aggregate

Payment

    Value Objects

PaymentId
Amount

3. DTOs
   PaymentRefundResponseDTO
   PaymentRequestDTO
   PaymentResponseDTO
   PaymentStateDTO

4. Events
   PaymentFailedEvent
   PaymentInitiatedEvent
   PaymentSucceededEvent
   RefundFailedEvent
   RefundIssuedEvent
   RefundSucceededEvent

5. Communication

DeliveryService → receive payment request + send paymentEvent

6. Typical Flow

DeliveryService request HTTP -> payment_service handling -> send Event back 