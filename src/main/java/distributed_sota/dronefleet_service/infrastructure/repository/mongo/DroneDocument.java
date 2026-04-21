package distributed_sota.dronefleet_service.infrastructure.repository.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import distributed_sota.dronefleet_service.domain.model.DroneState;

@Document(collection = "drones")
public class DroneDocument {

    @Id
    private String id;

    private String baseId;
    private int batteryPercent;
    private double latitude;
    private double longitude;
    private DroneState state;
    private double kilometrage;

    private String currentDeliveryId;
    private LocationData pickupLocation;
    private LocationData dropoffLocation;
    private LocationData returnBaseLocation;
    private String missionPhase;

    public DroneDocument() {
    }

    public DroneDocument(
            String id,
            String baseId,
            int batteryPercent,
            double latitude,
            double longitude,
            DroneState state,
            double kilometrage,
            String currentDeliveryId,
            LocationData pickupLocation,
            LocationData dropoffLocation,
            LocationData returnBaseLocation,
            String missionPhase
    ) {
        this.id = id;
        this.baseId = baseId;
        this.batteryPercent = batteryPercent;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
        this.kilometrage = kilometrage;
        this.currentDeliveryId = currentDeliveryId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.returnBaseLocation = returnBaseLocation;
        this.missionPhase = missionPhase;
    }

    public String getId() {
        return id;
    }

    public String getBaseId() {
        return baseId;
    }

    public int getBatteryPercent() {
        return batteryPercent;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public DroneState getState() {
        return state;
    }

    public double getKilometrage() {
        return kilometrage;
    }

    public String getCurrentDeliveryId() {
        return currentDeliveryId;
    }

    public LocationData getPickupLocation() {
        return pickupLocation;
    }

    public LocationData getDropoffLocation() {
        return dropoffLocation;
    }

    public LocationData getReturnBaseLocation() {
        return returnBaseLocation;
    }

    public String getMissionPhase() {
        return missionPhase;
    }

    public static class LocationData {
        private double lat;
        private double lon;

        public LocationData() {
        }

        public LocationData(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }
    }
}
