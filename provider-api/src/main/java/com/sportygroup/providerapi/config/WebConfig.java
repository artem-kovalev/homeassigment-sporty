package com.sportygroup.providerapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.providerapi.advice.CorrelationIdFilter;
import com.sportygroup.providerapi.advice.HttpLoggingFilter;
import com.sportygroup.providerapi.json.UseDeserializerArgumentResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    public WebConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.addFirst(new UseDeserializerArgumentResolver(objectMapper));
    }

    @Bean
    public FilterRegistrationBean<HttpLoggingFilter> httpLoggingFilterRegistration() {
        var bean = new FilterRegistrationBean<>(new HttpLoggingFilter());
        bean.setOrder(Integer.MIN_VALUE + 10);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
        FilterRegistrationBean<CorrelationIdFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new CorrelationIdFilter());
        bean.setOrder(Integer.MIN_VALUE);
        return bean;
    }
}