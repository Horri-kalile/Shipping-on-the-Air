package distributed_sota.api_gateway.infrastructure.proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;


public class DroneProxy extends HttpSyncBaseProxy {

    private final String baseUrl;

    public DroneProxy(RestTemplate restTemplate,
                      @Value("${services.dronefleet.base-url}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public Object createBase(String name, Double lat, Double lon, int capacity) {
        String url = baseUrl + "/base?name=" + name + "&latitude=" + lat + "&longitude=" + lon + "&capacity=" + capacity;
        return post(url, null, Object.class);
    }

    public Object createDrone(String baseId) {
        return post(baseUrl + "/drone?baseId=" + baseId, null, Object.class);
    }
}
