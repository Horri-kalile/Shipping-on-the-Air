package distributed_sota.payment_service.application.port;

import com.fasterxml.jackson.core.JsonProcessingException;
import common.hexagonal.OutBoundPort;
import distributed_sota.payment_service.domain.event.PaymentEvent;

@OutBoundPort
public interface EventPublisherPort {

    void publishEvent(PaymentEvent event) throws JsonProcessingException;
}
