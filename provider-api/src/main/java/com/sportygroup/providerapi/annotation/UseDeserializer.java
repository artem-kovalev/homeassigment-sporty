package com.sportygroup.providerapi.annotation;

import com.fasterxml.jackson.databind.JsonDeserializer;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseDeserializer {
    Class<? extends JsonDeserializer<?>> value();
}
