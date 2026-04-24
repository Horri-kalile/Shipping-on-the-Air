package distributed_sota.api_gateway.infrastructure.proxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

public class UserProxy extends HttpSyncBaseProxy {

    private final String baseUrl;

    public UserProxy(RestTemplate restTemplate,
                     @Value("${services.user.base-url}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public Object register(String name, String email, String password) {
        String url = baseUrl + "/register?name=" + name + "&email=" + email + "&password=" + password;
        return post(url, null, Object.class);
    }

    public Object getUserInfo(String userId) {
        return get(baseUrl + "/" + userId + "/info", Object.class);
    }
}
