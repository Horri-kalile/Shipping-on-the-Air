package distributed_sota.delivery_service.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import distributed_sota.delivery_service.application.port.DeliveryService;
import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.infrastructure.mapper.PaymentEventMapper;
@Component
public class PaymentEventListener {

    private final DeliveryService deliveryService;
    private final PaymentEventMapper paymentEventMapper;

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    public PaymentEventListener(
            DeliveryService deliveryService,
            PaymentEventMapper paymentEventMapper
    ) {
        this.deliveryService = deliveryService;
        this.paymentEventMapper = paymentEventMapper;
    }

    @KafkaListener(topics = "payment-events")
    public void onPaymentEvent(String json) {

        log.info("[KAFKA][DELIVERY] raw message received={}", json);

        try {
            PaymentEvent event = paymentEventMapper.fromJson(json);
            if (event == null) {
                log.info("[KAFKA][DELIVERY] payment event ignored");
                return;
            }

            DeliveryId id = new DeliveryId(event.deliveryId());

            switch (event.type()) {
                case PAYMENT_SUCCEEDED ->
                        deliveryService.onPaymentAccepted(id);

                case PAYMENT_FAILED ->
                        deliveryService.failDelivery(id, "Payment failed");

                default -> { }
            }

        } catch (Exception e) {
            log.error("[KAFKA][DELIVERY] failed to process payment event", e);
        }
    }
}
