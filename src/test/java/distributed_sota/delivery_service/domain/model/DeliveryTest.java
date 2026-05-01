package distributed_sota.delivery_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class DeliveryTest {

    @Test
    @DisplayName("Should transition from CREATED to PAYMENT_PENDING")
    void transitionToPaymentPending() throws Exception {
        DeliveryId id = new DeliveryId("order-1234");
        DeliveryRequest request = new DeliveryRequest("u-1", new Location(0,0), new Location(1,1), new Weight(1.0), Instant.now(), Instant.now().plusSeconds(3600));
        Delivery delivery = new Delivery(id, request, new ETA(Instant.now()), new Price(10.0, "EUR"));

        delivery.markPaymentPending();
        
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.PAYMENT_PENDING);
    }

    @Test
    @DisplayName("Should throw exception if drone requested before payment")
    void droneRequestBeforePayment() throws Exception {
        DeliveryId id = new DeliveryId("order-1234");
        DeliveryRequest request = new DeliveryRequest("u-1", new Location(0,0), new Location(1,1), new Weight(1.0), Instant.now(), Instant.now().plusSeconds(3600));
        Delivery delivery = new Delivery(id, request, new ETA(Instant.now()), new Price(10.0, "EUR"));

        assertThatThrownBy(delivery::requestDrone)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("before payment confirmation");
    }

    @Test
    @DisplayName("Should handle cancellation with refund correctly")
    void cancelWithRefund() throws Exception {
        DeliveryId id = new DeliveryId("order-1234");
        DeliveryRequest request = new DeliveryRequest("u-1", new Location(0,0), new Location(1,1), new Weight(1.0), Instant.now(), Instant.now().plusSeconds(3600));
        Delivery delivery = new Delivery(id, request, new ETA(Instant.now()), new Price(10.0, "EUR"));

        delivery.markPaymentPending();
        delivery.markPaymentConfirmed();
        
        var event = delivery.cancel();
        
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.CANCELED);
        assertThat(event.refundNeeded()).isTrue();
    }
}
