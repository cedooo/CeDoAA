package com.cedoo.springcloud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/***
 * spring boot starter配置
 */
@Configuration
@ConditionalOnClass({Docket.class, Swagger2Properties.class})
@EnableConfigurationProperties(Swagger2Properties.class)
@EnableSwagger2
@PropertySources({
    @PropertySource(value = "classpath:application-swagger2.properties",encoding = "utf-8")
})
public class SwaggerAutoConfiguration {

    // 注入属性类
    @Autowired
    private Swagger2Properties swagger2Properties;


    @ConditionalOnMissingBean(Docket.class)
    @Bean
    public Docket createRestApi() {
        System.out.println("初始化API文档服务");
        System.out.println("API文档配置信息：" + this.swagger2Properties.toString());
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(swagger2Properties.getGroupName())
                .apiInfo(apiInfo(swagger2Properties))
                .host(swagger2Properties.getHost())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swagger2Properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private static ApiInfo apiInfo(Swagger2Properties swagger2Properties) {
        Contact contact = new Contact(swagger2Properties.getContactName(), swagger2Properties.getContactUrl(), swagger2Properties.getContactEmail());
        return new ApiInfoBuilder()
                .title(swagger2Properties.getTitle())
                .description(swagger2Properties.getDesc())
                .termsOfServiceUrl(swagger2Properties.getServiceUrl())
                .contact(swagger2Properties.getContact())
                .contact(contact)
                .version(swagger2Properties.getVersion())
                .license(swagger2Properties.getLicense())
                .licenseUrl(swagger2Properties.getLicenseUrl())
                .build();
    }


}