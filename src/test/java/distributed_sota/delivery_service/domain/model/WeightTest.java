package distributed_sota.delivery_service.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class WeightTest {

    @Test
    @DisplayName("Should create valid weight")
    void validWeight() {
        Weight weight = new Weight(1.5);
        assertThat(weight.value()).isEqualTo(1.5);
    }

    @Test
    @DisplayName("Should throw error for negative weight")
    void negativeWeight() {
        assertThatThrownBy(() -> new Weight(-1.0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("positive");
    }

    @Test
    @DisplayName("Should throw error for weight over drone capacity (2.3kg)")
    void tooHeavyWeight() {
        assertThatThrownBy(() -> new Weight(2.5))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("2.3 kg");
    }
}
