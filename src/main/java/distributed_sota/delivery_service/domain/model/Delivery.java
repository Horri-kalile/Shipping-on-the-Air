package distributed_sota.delivery_service.domain.model;

import java.util.Optional;
import java.util.logging.Logger;

import common.ddd.Aggregate;
import distributed_sota.delivery_service.application.exception.DeliveryAlreadyCompletedException;
import distributed_sota.delivery_service.application.exception.DeliveryAlreadyStartedException;
import distributed_sota.delivery_service.domain.event.DeliveryCanceledEvent;
import distributed_sota.delivery_service.domain.event.DeliveryCompletedEvent;
import distributed_sota.delivery_service.domain.event.DeliveryFailedEvent;
import distributed_sota.delivery_service.domain.event.DeliveryStartedEvent;
import distributed_sota.delivery_service.domain.event.EtaUpdatedEvent;
import distributed_sota.delivery_service.domain.event.WaitForPickUpEvent;

public class Delivery implements Aggregate<DeliveryId> {

    private static final Logger logger = Logger.getLogger("[Delivery]");

    private final DeliveryId id;
    private final DeliveryRequest request;

    private DeliveryStatus status;
    private final Optional<String> droneId;

    private final Price price;
    private ETA eta;
    private RemainingDuration remainingDuration;
    private Location droneLocation;

    //-------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------
    public Delivery(DeliveryId id, DeliveryRequest request, ETA eta, Price price) {
        this(id, request, DeliveryStatus.CREATED, Optional.empty(), price, eta, new RemainingDuration(eta), null);
    }

    private Delivery(
            DeliveryId id,
            DeliveryRequest request,
            DeliveryStatus status,
            Optional<String> droneId,
            Price price,
            ETA eta,
            RemainingDuration remainingDuration,
            Location droneLocation
    ) {
        this.id = id;
        this.request = request;
        this.status = status;
        this.droneId = droneId;
        this.price = price;
        this.eta = eta;
        this.remainingDuration = remainingDuration;
        this.droneLocation = droneLocation;
    }

    public static Delivery rehydrate(
            DeliveryId id,
            DeliveryRequest request,
            DeliveryStatus status,
            Optional<String> droneId,
            Price price,
            ETA eta,
            RemainingDuration remainingDuration,
            Location droneLocation
    ) {
        return new Delivery(
                id,
                request,
                status,
                droneId,
                price,
                eta,
                remainingDuration,
                droneLocation
        );
    }

    //-------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    public EtaUpdatedEvent updateDroneLocation(Location location) {
        if (droneId.isEmpty()) {
            logger.warning(() -> String.format("Cannot update location for delivery %s: no drone assigned", id));
            throw new IllegalStateException("Cannot update location: no drone assigned");
        }
        this.droneLocation = location;
        logger.info(() -> String.format("Updated drone location for delivery %s", id));

        return EtaUpdatedEvent.from(id, request.userId(), eta, remainingDuration);
    }

    public void updateETA(ETA eta) {
        this.eta = eta;
    }

    public void updateRemainingDuration(RemainingDuration remainingDuration) {
        this.remainingDuration = remainingDuration;
    }

    //-------------------------------------------------------------------------
    // NEXT STEPS
    // -------------------------------------------------------------------------
    public void markPaymentPending() {
        if (status != DeliveryStatus.CREATED) {
            logger.warning(() -> String.format("Invalid transition to PAYMENT_PENDING from %s for delivery %s", status, id));
            throw new IllegalStateException("Payment can only be pending from CREATED state");
        }
        this.status = DeliveryStatus.PAYMENT_PENDING;
        logger.info(() -> String.format("Delivery %s marked as PAYMENT_PENDING", id));
    }

    public void markPaymentConfirmed() {
        if (status != DeliveryStatus.PAYMENT_PENDING) {
            logger.warning(() -> String.format("Invalid transition to PAYMENT_CONFIRMED from %s for delivery %s", status, id));
            throw new IllegalStateException("Payment cannot be confirmed from current state");
        }
        this.status = DeliveryStatus.PAYMENT_CONFIRMED;
        logger.info(() -> String.format("Delivery %s marked as PAYMENT_CONFIRMED", id));
    }

    public void requestDrone() {
        if (status != DeliveryStatus.PAYMENT_CONFIRMED) {
            logger.warning(() -> String.format("Cannot request drone from %s for delivery %s", status, id));
            throw new IllegalStateException("Cannot request drone before payment confirmation");
        }
        this.status = DeliveryStatus.DRONE_ASSIGNMENT_REQUESTED;
        logger.info(() -> String.format("Delivery %s requested drone assignment", id));
    }

    public WaitForPickUpEvent markWaitForPickUp() {
        if (status != DeliveryStatus.DRONE_ASSIGNMENT_REQUESTED) {
            logger.warning(() -> String.format("Cannot move to WAIT_FOR_PICKUP from %s for delivery %s", status, id));
            throw new IllegalStateException("Cannot pick up before drone is assigned");
        }
        this.status = DeliveryStatus.WAIT_FOR_PICKUP;
        logger.info(() -> String.format("Delivery %s waiting for pickup", id));
        return new WaitForPickUpEvent(id, request.userId());
    }

    public DeliveryStartedEvent markPickedUp() {
        if (status != DeliveryStatus.WAIT_FOR_PICKUP) {
            logger.warning(() -> String.format("Cannot mark picked up from %s for delivery %s", status, id));
            throw new IllegalStateException("Cannot pick up before drone is assigned");
        }
        this.status = DeliveryStatus.IN_PROGRESS;
        logger.info(() -> String.format("Delivery %s marked IN_PROGRESS", id));
        return new DeliveryStartedEvent(id, request.userId());
    }

    public DeliveryCompletedEvent markDelivered() {
        if (status != DeliveryStatus.IN_PROGRESS) {
            logger.warning(() -> String.format("Cannot complete delivery %s from state %s", id, status));
            throw new IllegalStateException("Delivery cannot be completed now");
        }
        this.status = DeliveryStatus.COMPLETED;
        logger.info(() -> String.format("Delivery %s marked COMPLETED", id));
        return new DeliveryCompletedEvent(id, request.userId());
    }

    public DeliveryCanceledEvent cancel() throws DeliveryAlreadyCompletedException, DeliveryAlreadyStartedException {
        if (this.status == DeliveryStatus.COMPLETED)
            throw new DeliveryAlreadyCompletedException(id);
        if (this.status == DeliveryStatus.IN_PROGRESS)
            throw new DeliveryAlreadyStartedException(id);

        boolean refundNeeded = (status == DeliveryStatus.PAYMENT_CONFIRMED ||
                status == DeliveryStatus.DRONE_ASSIGNMENT_REQUESTED ||
                status == DeliveryStatus.WAIT_FOR_PICKUP);

        this.status = DeliveryStatus.CANCELED;
        logger.info(() -> String.format("Delivery %s canceled%s", id, refundNeeded ? " with refund" : ""));

        return new DeliveryCanceledEvent(id, request.userId(), refundNeeded);
    }

    public DeliveryFailedEvent markFailed(String reason) throws DeliveryAlreadyCompletedException {
        if (this.status == DeliveryStatus.COMPLETED)
            throw new DeliveryAlreadyCompletedException(id);
        if (this.status == DeliveryStatus.CANCELED)
            throw new IllegalStateException("Delivery already canceled");
        this.status = DeliveryStatus.FAILED;
        logger.warning(() -> String.format("Delivery %s failed: %s", id, reason));
        return new DeliveryFailedEvent(id, request.userId(), reason);
    }

    //-------------------------------------------------------------------------
    // GETTERS
    // -------------------------------------------------------------------------
    public boolean isCompleted() { return status == DeliveryStatus.COMPLETED; }
    public boolean isStarted() { return status == DeliveryStatus.IN_PROGRESS; }
    public boolean isCanceled() { return status == DeliveryStatus.CANCELED; }
    public boolean isReadyToStart() { return droneId.isPresent(); }
    public Optional<Location> getDroneLocation() { return Optional.ofNullable(droneLocation); }

    @Override
    public DeliveryId getId() { return id; }
    public DeliveryRequest getRequest() { return request; }
    public DeliveryStatus getStatus() { return status; }
    public Optional<String> getDroneId() { return droneId; }
    public Price getPrice() { return price; }
    public RemainingDuration getRemainingDuration() { return remainingDuration; }
    public ETA getETA() { return eta; }
}
