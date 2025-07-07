package sesac.bookmanager.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:src/main/resources/static/images/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));

        // images 패턴 추가
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/")
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(365)));
    }

}