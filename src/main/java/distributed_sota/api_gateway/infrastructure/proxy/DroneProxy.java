package distributed_sota.api_gateway.infrastructure.proxy;
import org.springframework.web.client.RestTemplate;


public class DroneProxy extends HttpSyncBaseProxy {

    private final String BASE_URL = "http://dronefleet-service:8082/drones";

    public DroneProxy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public Object createBase(String name, Double lat, Double lon, int capacity) {
        String url = BASE_URL + "/base?name=" + name + "&latitude=" + lat + "&longitude=" + lon + "&capacity=" + capacity;
        return post(url, null, Object.class);
    }

    public Object createDrone(String baseId) {
        return post(BASE_URL + "/drone?baseId=" + baseId, null, Object.class);
    }
}