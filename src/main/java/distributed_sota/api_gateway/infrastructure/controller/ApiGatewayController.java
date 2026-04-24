package distributed_sota.api_gateway.infrastructure.controller;

import distributed_sota.api_gateway.infrastructure.proxy.*;
import org.gradle.api.component.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
@RequestMapping("/api")
public class ApiGatewayController {

    private final DeliveryProxy deliveryProxy;
    private final UserProxy userProxy;
    private final PaymentProxy paymentProxy;
    private final DroneProxy droneProxy;

    public ApiGatewayController(DeliveryProxy deliveryProxy,
                                UserProxy userProxy,
                                PaymentProxy paymentProxy,
                                DroneProxy droneProxy) {
        this.deliveryProxy = deliveryProxy;
        this.userProxy = userProxy;
        this.paymentProxy = paymentProxy;
        this.droneProxy = droneProxy;
    }

    // DELIVERY
    @PostMapping("/deliveries")
    public String createDelivery(@RequestBody Object dto) {
        return deliveryProxy.createDelivery(dto);
    }

    @PostMapping("/deliveries/{id}/start")
    public void startDelivery(@PathVariable String id) {
        deliveryProxy.startDelivery(id);
    }

    @GetMapping("/deliveries/{id}/status")
    public String status(@PathVariable String id) {
        return deliveryProxy.getStatus(id);
    }

    @GetMapping("/deliveries/{id}/remaining-time")
    public String eta(@PathVariable String id) {
        return deliveryProxy.getRemainingTime(id);
    }

    // USER
    @PostMapping("/users/register")
    public Object register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        return userProxy.register(name, email, password);
    }

    @GetMapping("/users/{id}")
    public Object getUser(@PathVariable String id) {
        return userProxy.getUserInfo(id);
    }

    // PAYMENT
    @PostMapping("/payments/{id}/start")
    public void startPayment(@PathVariable String id) {
        paymentProxy.startPayment(id);
    }

    @PostMapping("/payments/{id}/cancel")
    public void cancelPayment(@PathVariable String id) {
        paymentProxy.cancelPayment(id);
    }

    // DRONE
    @PostMapping("/drones/base")
    public Object createBase(@RequestParam String name,
                             @RequestParam Double lat,
                             @RequestParam Double lon,
                             @RequestParam int capacity) {
        return droneProxy.createBase(name, lat, lon, capacity);
    }

    @PostMapping("/drones/drone")
    public Object createDrone(@RequestParam String baseId) {
        return droneProxy.createDrone(baseId);
    }
}