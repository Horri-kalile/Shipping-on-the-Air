package distributed_sota.api_gateway.infrastructure.proxy;

import org.springframework.web.client.RestTemplate;

public abstract class HttpSyncBaseProxy {

    protected final RestTemplate restTemplate;

    protected HttpSyncBaseProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> T get(String url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    protected <T> T post(String url, Object body, Class<T> responseType) {
        return restTemplate.postForObject(url, body, responseType);
    }

    protected void put(String url, Object body) {
        restTemplate.put(url, body);
    }
}