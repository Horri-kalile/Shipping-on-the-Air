package distributed_sota.delivery_service.infrastructure.mapper;

import distributed_sota.delivery_service.application.dto.*;
import distributed_sota.delivery_service.application.exception.InvalidDeliveryRequestException;
import distributed_sota.delivery_service.domain.model.*;


public class DeliveryMapper {

    public static DeliveryRequest toRequest(DeliveryRequestDTO dto) throws InvalidDeliveryRequestException {
        return new DeliveryRequest(
                dto.userId(),
                new Location(dto.pickupLat(), dto.pickupLng()),
                new Location(dto.dropoffLat(), dto.dropoffLon()),
                new Weight(dto.weight()),
                dto.requestedStart(),
                dto.requestedEnd()
        );
    }

    public static DeliveryDTO toDTO(Delivery delivery) {
        return new DeliveryDTO(
                delivery.getId().id(),
                delivery.getRequest().pickupLocation().latitude(),
                delivery.getRequest().pickupLocation().longitude(),
                delivery.getRequest().dropoffLocation().latitude(),
                delivery.getRequest().dropoffLocation().longitude(),
                delivery.getRequest().weight().value(),
                delivery.getPrice().amount().doubleValue(),
                delivery.getPrice().currency().getCurrencyCode(),
                delivery.getETA().getArrivalTime()
        );
    }

    public static DeliveryStatusDTO toStatusDTO(Delivery delivery) {
        return new DeliveryStatusDTO(delivery.getId().id(), delivery.getStatus().name());
    }

    public static RemainingDurationDTO toRemainingDTO(Delivery delivery) {
        return new RemainingDurationDTO(
                delivery.getId().id(),
                delivery.getRemainingDuration().value().toMinutes()
        );
    }
}
