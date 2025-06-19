package pl.barMate;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

import java.util.List;

public class UserServiceSupplier implements ServiceInstanceListSupplier {
    private final String serviceId;

    UserServiceSupplier(String serviceId) {
        this.serviceId = serviceId;
    }
    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        ServiceInstance instance = new DefaultServiceInstance("001", serviceId, "localhost", 8084, false);
        return Flux.just(List.of(instance));
    }
}
