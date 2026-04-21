package distributed_sota.payment_service.infrastructure.repository.mongo;

import java.time.Instant;

import distributed_sota.payment_service.domain.model.Amount;
import distributed_sota.payment_service.domain.model.Payment;
import distributed_sota.payment_service.domain.model.PaymentId;

public final class PaymentDocumentMapper {

    private PaymentDocumentMapper() {
    }

    public static PaymentDocument toDocument(Payment payment) {
        return new PaymentDocument(
                payment.getPaymentId().toString(),
                payment.getUserId(),
                payment.getDeliveryId(),
                payment.getAmount().amount().doubleValue(),
                payment.getAmount().currency().toString(),
                payment.getWhenCreated(),
                payment.getState().name()
        );
    }

    public static Payment toDomain(PaymentDocument document) {
        Instant whenCreated = document.getWhenCreated() == null ? Instant.now() : document.getWhenCreated();
        Payment.PaymentState state = document.getState() == null
                ? Payment.PaymentState.PAYMENT_PENDING
                : Payment.PaymentState.valueOf(document.getState());

        return Payment.rehydrate(
                new PaymentId(document.getId()),
                document.getUserId(),
                document.getDeliveryId(),
                new Amount(document.getAmount(), document.getCurrency()),
                whenCreated,
                state
        );
    }
}
