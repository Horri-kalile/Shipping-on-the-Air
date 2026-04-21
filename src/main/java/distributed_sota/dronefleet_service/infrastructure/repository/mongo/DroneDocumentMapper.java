package distributed_sota.dronefleet_service.infrastructure.repository.mongo;

import distributed_sota.dronefleet_service.domain.model.BaseId;
import distributed_sota.dronefleet_service.domain.model.Battery;
import distributed_sota.dronefleet_service.domain.model.Drone;
import distributed_sota.dronefleet_service.domain.model.DroneId;
import distributed_sota.dronefleet_service.domain.model.DroneState;
import distributed_sota.dronefleet_service.domain.model.Location;

public final class DroneDocumentMapper {

    private DroneDocumentMapper() {
    }

    public static DroneDocument toDocument(Drone drone) {
        return new DroneDocument(
                drone.id().id(),
                drone.baseId().id(),
                drone.battery().percent(),
                drone.location().lat(),
                drone.location().lon(),
                drone.state(),
                drone.kilometrage(),
                drone.currentDeliveryId().orElse(null),
                toLocationData(drone.pickupLocation().orElse(null)),
                toLocationData(drone.dropoffLocation().orElse(null)),
                toLocationData(drone.returnBaseLocation().orElse(null)),
                drone.missionPhase().name()
        );
    }

    public static Drone toDomain(DroneDocument document) {
        DroneState state = document.getState() == null
                ? DroneState.AVAILABLE
                : document.getState();

        Drone.MissionPhase missionPhase = document.getMissionPhase() == null
                ? Drone.MissionPhase.NONE
                : Drone.MissionPhase.valueOf(document.getMissionPhase());

        return Drone.rehydrate(
                DroneId.is(document.getId()),
                BaseId.is(document.getBaseId()),
                new Battery(document.getBatteryPercent()),
                new Location(document.getLatitude(), document.getLongitude()),
                document.getKilometrage(),
                state,
                document.getCurrentDeliveryId(),
                toLocation(document.getPickupLocation()),
                toLocation(document.getDropoffLocation()),
                toLocation(document.getReturnBaseLocation()),
                missionPhase
        );
    }

    private static DroneDocument.LocationData toLocationData(Location location) {
        if (location == null) {
            return null;
        }
        return new DroneDocument.LocationData(location.lat(), location.lon());
    }

    private static Location toLocation(DroneDocument.LocationData locationData) {
        if (locationData == null) {
            return null;
        }
        return new Location(locationData.getLat(), locationData.getLon());
    }
}
