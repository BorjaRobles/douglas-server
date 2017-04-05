package douglas.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
@ComponentScan(basePackages = {"douglas.web.controller"})
public class WebConfig extends WebMvcConfigurerAdapter {

    // Add handlers for static resources 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/*").addResourceLocations("classpath:/static/index.html");
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        registry.addResourceHandler("/views/**").addResourceLocations("classpath:/static/views/");

    }

    public static final String[] staticResourcePaths = {"/WEB-INF/static/**"};
    public static final String[] dynamicResourcePaths = { "/rest.v1/**" };
    public static final String[] allResourcePaths = ArrayUtils.addAll(staticResourcePaths, dynamicResourcePaths);

    @Autowired
    private RootConfig rootConfig;


    @Bean
    public WebContentInterceptor dynamicCache() {
		// Disable caching on dynamic routes
        WebContentInterceptor dynamicCache = new WebContentInterceptor();
        CacheControl cc = CacheControl.maxAge(0, TimeUnit.MILLISECONDS).mustRevalidate();

        dynamicCache.setCacheControl(cc);
        return dynamicCache;
    }

    @Bean
    public WebContentInterceptor resourceCache() {
        // Enabling caching on static assets
        WebContentInterceptor resourceCache = new WebContentInterceptor();
        CacheControl cc = CacheControl
                .maxAge(7, TimeUnit.DAYS)
                .mustRevalidate();

        resourceCache.setCacheControl(cc);
        return resourceCache;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		// Expose interceptors to spring

        registry.addInterceptor(dynamicCache())
                .addPathPatterns("/**")
                .excludePathPatterns(allResourcePaths);

        registry.addInterceptor(resourceCache())
                .addPathPatterns(allResourcePaths);

    }

    // Using custom Jackson converter to handle Hibernate interaction better and avoid
    // serializing un-initialized Hibernate properties
    public MappingJackson2HttpMessageConverter jacksonMessageConverter(){
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        ObjectMapper mapper = new ObjectMapper();
        // Registering Hibernate4Module to support lazy objects
        mapper.registerModule(new Hibernate5Module());

        messageConverter.setObjectMapper(mapper);
        return messageConverter;

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // Add the custom HttpMessageConverter
        converters.add(jacksonMessageConverter());
        super.configureMessageConverters(converters);
    }

}
