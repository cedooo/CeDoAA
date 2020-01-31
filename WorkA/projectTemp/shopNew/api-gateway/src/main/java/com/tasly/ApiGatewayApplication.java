package com.tasly;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author chendong
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableEurekaClient
@EnableZuulProxy
@SpringCloudApplication
public class ApiGatewayApplication {
  
  public static void main(String[] args) {
    new SpringApplicationBuilder(ApiGatewayApplication.class).run(args);
  }
  
}