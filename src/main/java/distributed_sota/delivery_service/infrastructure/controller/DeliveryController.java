package distributed_sota.delivery_service.infrastructure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed_sota.delivery_service.application.dto.DeliveryRequestDTO;
import distributed_sota.delivery_service.application.dto.DeliveryStatusDTO;
import distributed_sota.delivery_service.application.dto.DroneAssignmentResponseDTO;
import distributed_sota.delivery_service.application.dto.RemainingDurationDTO;
import distributed_sota.delivery_service.application.exception.DeliveryNotFoundException;
import distributed_sota.delivery_service.application.port.DeliveryService;
import distributed_sota.delivery_service.domain.model.Delivery;
import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.domain.model.DeliveryStatus;
import distributed_sota.delivery_service.domain.model.Location;
import distributed_sota.delivery_service.domain.model.RemainingDuration;


@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;
    private static final Logger log = LoggerFactory.getLogger(DeliveryController.class);

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<String> createDelivery(@RequestBody DeliveryRequestDTO dto) throws Exception {
        Delivery delivery = deliveryService.createDelivery(dto);
        log.info("[DELIVERY][CREATE] Created delivery with id={}", delivery.getId().id());
        return ResponseEntity.ok(delivery.getId().id());
    }

    @PostMapping("/{deliveryId}/start")
    public ResponseEntity<Void> start(@PathVariable String deliveryId) throws Exception {
        log.info("[DELIVERY][START] request to start delivery with id={}",deliveryId);
        deliveryService.requestDeliveryStart(DeliveryId.of(deliveryId));
        log.info("[DELIVERY][START] started delivery with id={}",deliveryId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{deliveryId}/status")
    public ResponseEntity<DeliveryStatusDTO> getStatus(@PathVariable String deliveryId) throws DeliveryNotFoundException {
        log.info("[DELIVERY][STATUS] request status delivery with id={}",deliveryId);
        DeliveryStatus status = deliveryService.getDeliveryStatus(DeliveryId.of(deliveryId));
        log.info("[DELIVERY][STATUS] status delivery with id={}",deliveryId);
        return ResponseEntity.ok(new DeliveryStatusDTO(deliveryId,status.name()));
    }

    @GetMapping("/{deliveryId}/remaining-time")
    public ResponseEntity<RemainingDurationDTO> remaining(@PathVariable String deliveryId) throws DeliveryNotFoundException {
        log.info("[DELIVERY][REMAINING-TIME] request remaining-time delivery with id={}",deliveryId);
        RemainingDuration remaining = deliveryService.getTimeLeft(DeliveryId.of(deliveryId));
        log.info("[DELIVERY][REMAINING-TIME] remaining-time delivery with id={}",deliveryId);
        return ResponseEntity.ok(new RemainingDurationDTO(deliveryId,remaining.value().toMinutes()));
    }

    
    // TECH – used by DroneFleetService
    

    @GetMapping("/{deliveryId}/exists")
    public ResponseEntity<Boolean> deliveryExists(
            @PathVariable String deliveryId) {

        boolean exists = deliveryService.deliveryExists(DeliveryId.of(deliveryId));
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{deliveryId}/pickup")
    public ResponseEntity<LocationDTO> getPickupLocation(
            @PathVariable String deliveryId) throws DeliveryNotFoundException {

        Location pickup = deliveryService.getPickupLocation(DeliveryId.of(deliveryId));
        return ResponseEntity.ok(LocationDTO.from(pickup));
    }

    @GetMapping("/{deliveryId}/dropoff")
    public ResponseEntity<LocationDTO> getDropoffLocation(
            @PathVariable String deliveryId) throws DeliveryNotFoundException {

        Location dropoff = deliveryService.getDropoffLocation(DeliveryId.of(deliveryId));
        return ResponseEntity.ok(LocationDTO.from(dropoff));
    }

    @PostMapping("/{deliveryId}/assignDrone")
    public ResponseEntity<Void> assignDroneToDelivery(
            @PathVariable String deliveryId,
            @RequestBody DroneAssignmentResponseDTO request) throws DeliveryNotFoundException {

        deliveryService.onDroneAssigned(
                DeliveryId.of(deliveryId),
                request.droneId()
        );
        return ResponseEntity.ok().build();
    }

    public record LocationDTO(double lat, double lon) {
        public static LocationDTO from(Location l) {
            return new LocationDTO(l.latitude(), l.longitude());
        }
    }

}
