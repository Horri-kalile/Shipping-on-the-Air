package distributed_sota.delivery_service.domain.model;

import common.ddd.ValueObject;
import java.time.Duration;

public class RemainingDuration implements ValueObject {
    private final Duration remaining;

    public RemainingDuration(ETA eta) {
        this.remaining = eta.remainingFromNow();
    }

    public Duration value() {
        return remaining;
    }
}
