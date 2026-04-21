package distributed_sota.payment_service.infrastructure.repository.mongo;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class PaymentDocument {

    @Id
    private String id;

    private String userId;
    private String deliveryId;
    private double amount;
    private String currency;
    private Instant whenCreated;
    private String state;

    public PaymentDocument() {
    }

    public PaymentDocument(
            String id,
            String userId,
            String deliveryId,
            double amount,
            String currency,
            Instant whenCreated,
            String state
    ) {
        this.id = id;
        this.userId = userId;
        this.deliveryId = deliveryId;
        this.amount = amount;
        this.currency = currency;
        this.whenCreated = whenCreated;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getWhenCreated() {
        return whenCreated;
    }

    public String getState() {
        return state;
    }
}
