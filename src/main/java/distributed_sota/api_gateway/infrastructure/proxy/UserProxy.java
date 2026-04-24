package distributed_sota.api_gateway.infrastructure.proxy;
import org.gradle.api.component.Component;
import org.springframework.web.client.RestTemplate;

public class UserProxy extends HttpSyncBaseProxy {

    private final String BASE_URL = "http://user-service:8081/users";

    public UserProxy(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public Object register(String name, String email, String password) {
        String url = BASE_URL + "/register?name=" + name + "&email=" + email + "&password=" + password;
        return post(url, null, Object.class);
    }

    public Object getUserInfo(String userId) {
        return get(BASE_URL + "/" + userId + "/info", Object.class);
    }
}