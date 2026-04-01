package distributed_sota.payment_service.domain.event;

import java.time.Instant;

public interface PaymentEvent { String type(); Instant occurredAt(); }
