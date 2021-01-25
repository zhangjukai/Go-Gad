package com.zjk.hy.config;

import org.springframework.cloud.gateway.filter.factory.StripPrefixGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder) {
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("provider-payment", r -> r.path("/pay/**")
                        .filters(f->f.stripPrefix(1))
                        //.uri("http://localhost:8001")
                .uri("lb://CLOUD-PROVIDER-PAYMENT")
                )
                .build();
        return routes.build();
    }
}
