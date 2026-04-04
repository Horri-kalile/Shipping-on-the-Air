package distributed_sota.delivery_service.domain.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import common.ddd.DomainService;
import distributed_sota.delivery_service.domain.model.Distance;
import distributed_sota.delivery_service.domain.model.ETA;
import distributed_sota.delivery_service.domain.model.Location;
import distributed_sota.delivery_service.domain.model.Price;
import distributed_sota.delivery_service.domain.model.Weight;

public class DeliveryCalculator implements DomainService {

    private static final double BASE_RATE_PER_KM = 1.2;
    private static final double WEIGHT_SURCHARGE_RATE = 0.05; // +5% per kg
    private static final int DRONE_AVG_SPEED_KMH = 60;


    public Price computePrice(Distance distance, Weight weight) {
        double base = distance.kilometers() * BASE_RATE_PER_KM;
        double surcharge = base * (weight.value() * WEIGHT_SURCHARGE_RATE);
        return new Price(base + surcharge, "EUR");
    }


    public ETA computeETA(Distance distance) {
        double hours = distance.kilometers() / DRONE_AVG_SPEED_KMH;
        long minutes = Math.round(hours * 60);
        Instant etaInstant = Instant.now().plus(Duration.ofMinutes(minutes));
        return new ETA(etaInstant);
    }


    public Distance computeDistance(Location pickup, Location dropoff) {
        return Distance.between(pickup, dropoff);
    }

    public ETA updateETA(Location currentLocation, Location dropoff) {
        Distance remaining = Distance.between(currentLocation, dropoff);
        return computeETA(remaining);
    }


    /**
     * @param eta       Heure d'arrivée estimée
     * @param timeStart Début de la plage demandée (Optional)
     * @param timeEnd   Fin de la plage demandée (Optional)
     * @return true si ETA est dans la plage demandée ou si la plage n'est pas définie
     */
    public boolean isFeasible(ETA eta, Optional<LocalDateTime> timeStart, Optional<LocalDateTime> timeEnd) {
        if (eta == null || eta.getArrivalTime() == null) {
            return false;
        }

        // Convertir l'Instant de ETA en LocalDateTime
        LocalDateTime arrival = LocalDateTime.ofInstant(eta.getArrivalTime(), ZoneId.systemDefault());

        return (!timeStart.isPresent() || !arrival.isBefore(timeStart.get()))
                && (!timeEnd.isPresent() || !arrival.isAfter(timeEnd.get()));
    }

}
