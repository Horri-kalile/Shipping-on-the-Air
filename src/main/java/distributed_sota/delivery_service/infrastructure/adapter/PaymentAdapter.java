package distributed_sota.delivery_service.infrastructure.adapter;

import distributed_sota.delivery_service.application.exception.PaymentFailedException;
import distributed_sota.delivery_service.application.exception.RefundFailedException;
import distributed_sota.delivery_service.application.port.PaymentPort;
import distributed_sota.delivery_service.application.dto.PaymentRequestDTO;
import distributed_sota.delivery_service.application.service.DeliveryServiceImpl;
import distributed_sota.delivery_service.domain.model.DeliveryId;
import distributed_sota.delivery_service.domain.model.Price;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentAdapter implements PaymentPort {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    private static final Logger log = LoggerFactory.getLogger(DeliveryServiceImpl.class);


    public PaymentAdapter(RestTemplate restTemplate,
                          @Value("${services.payment.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    @Override
    public void requestPayment(String userId, DeliveryId deliveryId, Price amount) throws PaymentFailedException {
        PaymentRequestDTO request = new PaymentRequestDTO(
                userId,
                deliveryId.id(),
                amount.amount().doubleValue(),
                amount.currency().toString()
        );

        try {
            restTemplate.postForEntity(baseUrl + "/payment", request, Void.class);
        } catch (HttpStatusCodeException e) {
            log.error("Payment error status={}, body={}",
                    e.getStatusCode(),
                    e.getResponseBodyAsString()
            );
            throw new PaymentFailedException();
        }

    }

    @Override
    public void requestRefund(String userId, DeliveryId deliveryId, Price amount) throws RefundFailedException {
        PaymentRequestDTO request = new PaymentRequestDTO(
                userId,
                deliveryId.id(),
                amount.amount().doubleValue(),
                amount.currency().toString()
        );

        try {
            restTemplate.postForObject(baseUrl + "/refund", request, Void.class);
        } catch (RestClientException e) {
            throw new RefundFailedException();
        }
    }
}
