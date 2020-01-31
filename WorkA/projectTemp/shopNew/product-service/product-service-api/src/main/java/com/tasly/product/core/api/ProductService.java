package com.tasly.product.core.api;


import org.springframework.cloud.openfeign.FeignClient;

/**
 * Created by dulei on 18/1/9.
 */
@FeignClient(value = "product-service")
public interface ProductService {

}
