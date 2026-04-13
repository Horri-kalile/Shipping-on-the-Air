package distributed_sota.delivery_service.infrastructure.repository.mongo;

import java.time.Duration;
import java.util.Optional;

import distributed_sota.delivery_service.application.exception.InvalidDeliveryRequestException;
import distributed_sota.delivery_service.domain.model.Delivery;
import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.domain.model.DeliveryRequest;
import distributed_sota.delivery_service.domain.model.DeliveryStatus;
import distributed_sota.delivery_service.domain.model.ETA;
import distributed_sota.delivery_service.domain.model.Location;
import distributed_sota.delivery_service.domain.model.Price;
import distributed_sota.delivery_service.domain.model.RemainingDuration;
import distributed_sota.delivery_service.domain.model.Weight;

public final class DeliveryDocumentMapper {

    private DeliveryDocumentMapper() {
    }

    public static DeliveryDocument toDocument(Delivery delivery) {
        DeliveryDocument document = new DeliveryDocument();
        document.setId(delivery.getId().id());
        document.setUserId(delivery.getRequest().userId());
        document.setStatus(delivery.getStatus().name());
        document.setDroneId(delivery.getDroneId().orElse(null));

        DeliveryDocument.RequestData requestData = new DeliveryDocument.RequestData();
        requestData.setUserId(delivery.getRequest().userId());
        requestData.setPickupLocation(toLocationData(delivery.getRequest().pickupLocation()));
        requestData.setDropoffLocation(toLocationData(delivery.getRequest().dropoffLocation()));
        requestData.setWeight(delivery.getRequest().weight().value());
        requestData.setRequestedStart(delivery.getRequest().requestedStart());
        requestData.setRequestedEnd(delivery.getRequest().requestedEnd());
        document.setRequest(requestData);

        DeliveryDocument.PriceData priceData = new DeliveryDocument.PriceData();
        priceData.setAmount(delivery.getPrice().amount().doubleValue());
        priceData.setCurrency(delivery.getPrice().currency().getCurrencyCode());
        document.setPrice(priceData);

        document.setEtaArrivalTime(delivery.getETA().getArrivalTime());
        document.setRemainingDurationSeconds(delivery.getRemainingDuration().value().getSeconds());
        document.setDroneLocation(delivery.getDroneLocation().map(DeliveryDocumentMapper::toLocationData).orElse(null));

        return document;
    }

    public static Delivery toDomain(DeliveryDocument document) {
        DeliveryDocument.RequestData requestData = document.getRequest();

        DeliveryRequest request;
        try {
            request = new DeliveryRequest(
                    requestData.getUserId(),
                    toLocation(requestData.getPickupLocation()),
                    toLocation(requestData.getDropoffLocation()),
                    new Weight(requestData.getWeight()),
                    requestData.getRequestedStart(),
                    requestData.getRequestedEnd()
            );
        } catch (InvalidDeliveryRequestException ex) {
            throw new IllegalStateException("Stored delivery request is invalid", ex);
        }

        Price price = new Price(
                document.getPrice().getAmount(),
                document.getPrice().getCurrency()
        );

        ETA eta = new ETA(document.getEtaArrivalTime());

        long seconds = Math.max(document.getRemainingDurationSeconds(), 0L);
        RemainingDuration remainingDuration = new RemainingDuration(
                new ETA(document.getEtaArrivalTime().minus(Duration.ofSeconds(seconds)))
        );

        return Delivery.rehydrate(
                DeliveryId.of(document.getId()),
                request,
                DeliveryStatus.valueOf(document.getStatus()),
                Optional.ofNullable(document.getDroneId()),
                price,
                eta,
                remainingDuration,
                document.getDroneLocation() == null ? null : toLocation(document.getDroneLocation())
        );
    }

    private static DeliveryDocument.LocationData toLocationData(Location location) {
        DeliveryDocument.LocationData data = new DeliveryDocument.LocationData();
        data.setLatitude(location.latitude());
        data.setLongitude(location.longitude());
        return data;
    }

    private static Location toLocation(DeliveryDocument.LocationData data) {
        return new Location(data.getLatitude(), data.getLongitude());
    }
}
