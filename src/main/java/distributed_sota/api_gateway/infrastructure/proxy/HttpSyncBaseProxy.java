package distributed_sota.api_gateway.infrastructure.proxy;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

public abstract class HttpSyncBaseProxy {

    protected final RestTemplate restTemplate;

    protected HttpSyncBaseProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> T get(String url, Class<T> responseType) {
        try {
            return restTemplate.getForObject(url, responseType);
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }

    protected <T> T post(String url, Object body, Class<T> responseType) {
        try {
            return restTemplate.postForObject(url, body, responseType);
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }

    protected void put(String url, Object body) {
        try {
            restTemplate.put(url, body);
        } catch (HttpStatusCodeException ex) {
            throw new ResponseStatusException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        }
    }
}
