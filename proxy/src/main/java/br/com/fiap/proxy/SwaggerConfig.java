package br.com.fiap.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {

    @Autowired
    RouteLocator routeLocator;

//    @Bean
//    public PatternServiceRouteMapper serviceRouteMapper() {
//        return new PatternServiceRouteMapper(
//                "(?<name>^.+)-(?<version>v.+$)",
//                "${version}/${name}");
//    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .select()
//                .apis(RequestHandlerSelectors.any())
//                .paths(PathSelectors.any()).build().pathMapping("/")
                ;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Microservices Netflix - FIAP 2020 - 2DVP")
                .description("Sistema de microsserviços com casos Netflix para o curso Devops, FIAP 2020 - 2DVP")
                .termsOfServiceUrl("http://localhost:8081")
                .version("1.0")
                .build();
    }

    @Override
    public List<SwaggerResource> get() {
        //Dynamic introduction of micro services using routeLocator
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("zuul-gateway","/v2/api-docs","1.0"));
        //Recycling Lambda expressions to simplify code
        routeLocator.getRoutes().forEach(route -> {
            String name = route.getId();
            final String fullPath = route.getFullPath();
            String location = fullPath;//.replace("/api/apidocs", "/apidocs");
            location = location.replace("**", "v2/api-docs");
            //Dynamic acquisition
            if (route.getId().endsWith("-api-docs")) {
                resources.add(
                        swaggerResource(
                                name, location, "1.0"
                        )
                );
            }
        });
        //You can also directly inherit the Consumer interface
//		routeLocator.getRoutes().forEach(new Consumer<Route>() {
//
//			@Override
//			public void accept(Route t) {
//				// TODO Auto-generated method stub
//
//			}
//		});
        return resources;
    }

    private SwaggerResource swaggerResource(String name,String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }

//    @Override
//    public List<SwaggerResource> get() {
//        List resources = new ArrayList<>();
//        resources.add(swaggerResource("clientes-api-doc", "/clientes/v2/api-docs", "1.0"));
//        resources.add(swaggerResource("filmes-api-doc", "/filmes/v2/api-docs", "1.0"));
//        resources.add(swaggerResource("servicos-api-doc", "/servicos/v2/api-docs", "1.0"));
//        return resources;
//    }


}