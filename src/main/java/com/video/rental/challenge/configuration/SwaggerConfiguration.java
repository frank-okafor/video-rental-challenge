package com.video.rental.challenge.configuration;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ComponentScan
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
	return new Docket(DocumentationType.SWAGGER_2).protocols(new HashSet<>(Arrays.asList("HTTP")))
		.apiInfo(metaData()).select().apis(RequestHandlerSelectors.basePackage("com.video.rental.challenge"))
		.paths(PathSelectors.any()).build();
    }

    private ApiInfo metaData() {
	return new ApiInfoBuilder().title("Project Video Rental Challenge").description(
		"REST API for Video Rental Challenge. " + "Refer to use case documentation for more information")
		.build();
    }

}
