package distributed_sota.delivery_service.infrastructure.repository.mongo;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deliveries")
public class DeliveryDocument {

    @Id
    private String id;
    private String userId;
    private String status;
    private String droneId;

    private RequestData request;
    private PriceData price;

    private Instant etaArrivalTime;
    private long remainingDurationSeconds;

    private LocationData droneLocation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public RequestData getRequest() {
        return request;
    }

    public void setRequest(RequestData request) {
        this.request = request;
    }

    public PriceData getPrice() {
        return price;
    }

    public void setPrice(PriceData price) {
        this.price = price;
    }

    public Instant getEtaArrivalTime() {
        return etaArrivalTime;
    }

    public void setEtaArrivalTime(Instant etaArrivalTime) {
        this.etaArrivalTime = etaArrivalTime;
    }

    public long getRemainingDurationSeconds() {
        return remainingDurationSeconds;
    }

    public void setRemainingDurationSeconds(long remainingDurationSeconds) {
        this.remainingDurationSeconds = remainingDurationSeconds;
    }

    public LocationData getDroneLocation() {
        return droneLocation;
    }

    public void setDroneLocation(LocationData droneLocation) {
        this.droneLocation = droneLocation;
    }

    public static class RequestData {
        private String userId;
        private LocationData pickupLocation;
        private LocationData dropoffLocation;
        private double weight;
        private Instant requestedStart;
        private Instant requestedEnd;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public LocationData getPickupLocation() {
            return pickupLocation;
        }

        public void setPickupLocation(LocationData pickupLocation) {
            this.pickupLocation = pickupLocation;
        }

        public LocationData getDropoffLocation() {
            return dropoffLocation;
        }

        public void setDropoffLocation(LocationData dropoffLocation) {
            this.dropoffLocation = dropoffLocation;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public Instant getRequestedStart() {
            return requestedStart;
        }

        public void setRequestedStart(Instant requestedStart) {
            this.requestedStart = requestedStart;
        }

        public Instant getRequestedEnd() {
            return requestedEnd;
        }

        public void setRequestedEnd(Instant requestedEnd) {
            this.requestedEnd = requestedEnd;
        }
    }

    public static class LocationData {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class PriceData {
        private double amount;
        private String currency;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }
}
