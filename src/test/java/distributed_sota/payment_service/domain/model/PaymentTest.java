package distributed_sota.payment_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("Should create a valid payment in PENDING state")
    void createValidPayment() {
        PaymentId id = new PaymentId("pay-123");
        Amount amount = new Amount(100.0, "EUR");
        Payment payment = Payment.create(id, "user-456", "del-789", amount);

        assertThat(payment.getPaymentId()).isEqualTo(id);
        assertThat(payment.getState()).isEqualTo(Payment.PaymentState.PAYMENT_PENDING);
    }

    @Test
    @DisplayName("Should transition to CONFIRMED when payment is successful")
    void transitionToConfirmed() {
        Payment payment = Payment.create(new PaymentId("p-1"), "u-1", "d-1", new Amount(10.0, "EUR"));
        
        payment.markPaymentConfirmed();
        
        assertThat(payment.getState()).isEqualTo(Payment.PaymentState.PAYMENT_CONFIRMED);
    }

    @Test
    @DisplayName("Should throw exception when refunding a PENDING payment")
    void invalidRefundTransition() {
        Payment payment = Payment.create(new PaymentId("p-1"), "u-1", "d-1", new Amount(10.0, "EUR"));
        
        assertThatThrownBy(payment::markRefundIssued)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("only be issued for confirmed payments");
    }

    @Test
    @DisplayName("Should throw exception for negative amount")
    void negativeAmount() {
        assertThatThrownBy(() -> new Amount(-1.0, "EUR"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("non-negative");
    }
}
